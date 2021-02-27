package com.makotora.cardcostapi.service.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.makotora.cardcostapi.controller.response.CardClearingCostResponse;
import com.makotora.cardcostapi.exception.APIException;
import com.makotora.cardcostapi.exception.cardinfo.InvalidCardNumberException;
import com.makotora.cardcostapi.exception.countryclearingcost.CountryClearingCostNotFoundException;
import com.makotora.cardcostapi.exception.countryclearingcost.InvalidCountryCodeException;
import com.makotora.cardcostapi.service.CardClearingCostService;
import com.makotora.cardcostapi.service.CardInfoService;
import com.makotora.cardcostapi.service.CountryClearingCostService;

@Service
public class CardClearingCostServiceImpl implements CardClearingCostService
{
    private final Logger log;

    private final CardInfoService cardInfoService;

    private final CountryClearingCostService countryClearingCostService;

    @Autowired
    public CardClearingCostServiceImpl(
        CardInfoService cardInfoService,
        CountryClearingCostService countryClearingCostService)
    {
        log = LoggerFactory.getLogger(CardClearingCostServiceImpl.class);
        this.cardInfoService = cardInfoService;
        this.countryClearingCostService = countryClearingCostService;
    }

    @Override
    public CardClearingCostResponse determineClearingCost(String cardNumber)
        throws InvalidCardNumberException, CountryClearingCostNotFoundException, InvalidCountryCodeException,
        APIException
    {
        log.debug("determineClearingCost [cardNumber={}]", cardNumber);
        
        String countryCode = cardInfoService.getCountryCode(cardNumber);
        log.info("Determined country code {} for card number {}", countryCode, cardNumber);

        BigDecimal cost = countryClearingCostService.determineClearingCost(countryCode);
        log.info("Determined clearing cost: {}", cost);
        
        return new CardClearingCostResponse(countryCode, cost);
    }

}
