package com.makotora.cardcostapi.client;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.makotora.cardcostapi.client.response.BinlistAPIResponse;
import com.makotora.cardcostapi.exception.APIException;
import com.makotora.cardcostapi.exception.binlistclient.BinlistClientCountryCodeNotFoundException;
import com.makotora.cardcostapi.exception.binlistclient.BinlistClientRequestException;
import com.makotora.cardcostapi.exception.binlistclient.BinlistClientWaitTimeoutException;
import com.makotora.cardcostapi.ratelimit.PeriodRateLimiter;
import com.makotora.cardcostapi.service.IINCountryCodeProviderService;

@Service
public class BinlistAPIClient implements IINCountryCodeProviderService
{
    private final Logger log;

    private final RestTemplate restTemplate;

    private final String apiUrl;

    private PeriodRateLimiter binlistPeriodRateLimiter;

    /** If set represents the maximum number of milliseconds that we
     * should wait to acquire a permit from the rate limiter.
     * If we fail to acquire a permit an exception is thrown
     * that indicates that server is under heavy load. 
     */
    private final @Nullable Long slaWaitTimeoutMilliseconds;

    @Autowired
    public BinlistAPIClient(RestTemplateBuilder restTemplateBuilder)
    {
        log = LoggerFactory.getLogger(BinlistAPIClient.class);
        this.restTemplate = restTemplateBuilder.build();
        this.apiUrl = "https://lookup.binlist.net";

        /* The BINLIST API allows for up to 10 requests
         * per minute with a burst allowance of 10.
         * Create duration rate limiter with 10 doses available
         * that refreshes every minute.   
         */
        long maxBinlistRequestsPerMinute = 10;

        this.binlistPeriodRateLimiter = new PeriodRateLimiter(maxBinlistRequestsPerMinute,
                                                              Duration.ofMinutes(1));

        long maxIncomingRequestsPerMinute = 7000;

        if (maxIncomingRequestsPerMinute <= maxBinlistRequestsPerMinute) {
            // No waiting needed. If rate limit doesn't allow it then we shouldn't allow it either
            this.slaWaitTimeoutMilliseconds = 100l;
        }
        else {
            /* To be able to handle maxIncomingRequestsPerMinute:
             * The first maxBinlistRequestsPerMinute can be handled immediately.
             * The rest were #rest = (maxIncomingRequestsPerMinute - maxBinlistRequestsPerMinute)
             * need to wait at most (#rest / maxBinlistRequestsPerMinute) minutes
             * e.g. 10 binlist requests per minute, 30 max requests
             * the first 10 don't need to wait. The rest need to wait at most 20 / 10 = 2 minutes
             */
            this.slaWaitTimeoutMilliseconds = (maxIncomingRequestsPerMinute - maxBinlistRequestsPerMinute)
                                              * 60
                                              * 1000
                                              / maxBinlistRequestsPerMinute;
        }

    }

    @Override
    public String getCountryCode(String issuerIdNumber)
        throws APIException
    {
        log.debug("getCountryCode [issuerIdNumber={}]", issuerIdNumber);

        waitForBinlistSLA();

        BinlistAPIResponse response;

        try {
            response = getApiResponse(issuerIdNumber);
        }
        catch (Exception e) {
            log.error("Error getting BINLIST API response.");
            throw new BinlistClientRequestException("External API error. Could not find country code for identification number '"
                                                    + issuerIdNumber
                                                    + "'.",
                                                    e);
        }

        if (response == null
            || response.getCountry() == null
            || response.getCountry().getCode() == null
            || response.getCountry().getCode().trim().isEmpty()) {
            throw new BinlistClientCountryCodeNotFoundException("No country code found for issuer identification number '"
                                                                + issuerIdNumber
                                                                + "'.");
        }

        return response.getCountry().getCode().trim();
    }

    private void waitForBinlistSLA()
        throws BinlistClientWaitTimeoutException
    {
        if (slaWaitTimeoutMilliseconds == null) {
            binlistPeriodRateLimiter.consume();
            return;
        }

        boolean permitAcquired = binlistPeriodRateLimiter.tryConsume(slaWaitTimeoutMilliseconds,
                                                                     TimeUnit.MILLISECONDS);

        if (!permitAcquired) {
            throw new BinlistClientWaitTimeoutException("Reached wait timeout while trying to acquire a permit for the BINLIST API rate limiter");
        }
    }

    public BinlistAPIResponse getApiResponse(String issuerIdNumber)
    {
        return restTemplate.getForObject(apiUrl + "/{issuerIdNumber}",
                                         BinlistAPIResponse.class,
                                         issuerIdNumber);
    }

}
