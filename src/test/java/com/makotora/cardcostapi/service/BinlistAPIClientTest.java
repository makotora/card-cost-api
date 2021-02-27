package com.makotora.cardcostapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.makotora.cardcostapi.client.BinlistAPIClient;
import com.makotora.cardcostapi.client.response.BinlistAPIResponse;
import com.makotora.cardcostapi.enums.ErrorCode;
import com.makotora.cardcostapi.exception.APIException;
import com.makotora.cardcostapi.exception.binlistclient.BinlistClientCountryCodeNotFoundException;
import com.makotora.cardcostapi.exception.binlistclient.BinlistClientRequestException;
import com.makotora.cardcostapi.ratelimit.PeriodRateLimiter;

@ExtendWith(MockitoExtension.class)
public class BinlistAPIClientTest
{
    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    PeriodRateLimiter binlistPeriodRateLimiter;

    @Test
    public void getCountryCode_success()
        throws APIException
    {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        BinlistAPIClient binlistAPIClient = new BinlistAPIClient(restTemplateBuilder);

        String countryCode = "GR";
        
        BinlistAPIResponse binlistAPIResponse = new BinlistAPIResponse();
        BinlistAPIResponse.Country country = binlistAPIResponse.new Country();
        country.setCode(countryCode);
        binlistAPIResponse.setCountry(country);
        
        String issuerIdNumber = "1234 5678 1234 5678";
        
        when(binlistAPIClient.getApiResponse(issuerIdNumber)).thenReturn(binlistAPIResponse);
        
        String result = binlistAPIClient.getCountryCode(issuerIdNumber);
        
        assertEquals(countryCode, result);
    }
    
    @Test
    public void getCountryCode_null_country()
        throws APIException
    {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        BinlistAPIClient binlistAPIClient = new BinlistAPIClient(restTemplateBuilder);

        BinlistAPIResponse binlistAPIResponse = new BinlistAPIResponse();
        binlistAPIResponse.setCountry(null);

        String issuerIdNumber = "1234 5678 1234 5678";

        when(binlistAPIClient.getApiResponse(issuerIdNumber)).thenReturn(binlistAPIResponse);

        var actualEx = assertThrows(BinlistClientCountryCodeNotFoundException.class, () -> {
            binlistAPIClient.getCountryCode(issuerIdNumber);
        });

        assertEquals(ErrorCode.BINLIST_CLIENT_COUNTRY_CODE_NOT_FOUND, actualEx.getErrorCode());
    }

    @Test
    public void getCountryCode_null_country_code()
        throws APIException
    {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        BinlistAPIClient binlistAPIClient = new BinlistAPIClient(restTemplateBuilder);

        String countryCode = null;

        BinlistAPIResponse binlistAPIResponse = new BinlistAPIResponse();
        BinlistAPIResponse.Country country = binlistAPIResponse.new Country();
        country.setCode(countryCode);
        binlistAPIResponse.setCountry(country);
        
        String issuerIdNumber = "1234 5678 1234 5678";
        
        when(binlistAPIClient.getApiResponse(issuerIdNumber)).thenReturn(binlistAPIResponse);
        
        var actualEx = assertThrows(BinlistClientCountryCodeNotFoundException.class, () -> {
            binlistAPIClient.getCountryCode(issuerIdNumber);
        });

        assertEquals(ErrorCode.BINLIST_CLIENT_COUNTRY_CODE_NOT_FOUND, actualEx.getErrorCode());
    }

    @Test
    public void getCountryCode_empty_country_code()
        throws APIException
    {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        BinlistAPIClient binlistAPIClient = new BinlistAPIClient(restTemplateBuilder);

        String countryCode = "";

        BinlistAPIResponse binlistAPIResponse = new BinlistAPIResponse();
        BinlistAPIResponse.Country country = binlistAPIResponse.new Country();
        country.setCode(countryCode);
        binlistAPIResponse.setCountry(country);

        String issuerIdNumber = "1234 5678 1234 5678";

        when(binlistAPIClient.getApiResponse(issuerIdNumber)).thenReturn(binlistAPIResponse);

        var actualEx = assertThrows(BinlistClientCountryCodeNotFoundException.class, () -> {
            binlistAPIClient.getCountryCode(issuerIdNumber);
        });

        assertEquals(ErrorCode.BINLIST_CLIENT_COUNTRY_CODE_NOT_FOUND, actualEx.getErrorCode());
    }

    @Test
    public void getCountryCode_null_result()
        throws APIException
    {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        BinlistAPIClient binlistAPIClient = new BinlistAPIClient(restTemplateBuilder);

        String issuerIdNumber = "1234 5678 1234 5678";

        when(binlistAPIClient.getApiResponse(issuerIdNumber)).thenReturn(null);

        var actualEx = assertThrows(BinlistClientCountryCodeNotFoundException.class, () -> {
            binlistAPIClient.getCountryCode(issuerIdNumber);
        });

        assertEquals(ErrorCode.BINLIST_CLIENT_COUNTRY_CODE_NOT_FOUND, actualEx.getErrorCode());
    }

    @Test
    public void getCountryCode_request_exception()
        throws APIException
    {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        BinlistAPIClient binlistAPIClient = new BinlistAPIClient(restTemplateBuilder);

        String issuerIdNumber = "1234 5678 1234 5678";

        when(binlistAPIClient.getApiResponse(issuerIdNumber)).thenThrow(RuntimeException.class);

        var actualEx = assertThrows(BinlistClientRequestException.class, () -> {
            binlistAPIClient.getCountryCode(issuerIdNumber);
        });

        assertEquals(ErrorCode.BINLIST_CLIENT_REQUEST_ERROR, actualEx.getErrorCode());
    }

}
