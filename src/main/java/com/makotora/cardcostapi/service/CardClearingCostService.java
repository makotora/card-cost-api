package com.makotora.cardcostapi.service;

import org.springframework.lang.NonNull;

import com.makotora.cardcostapi.controller.response.CardClearingCostResponse;
import com.makotora.cardcostapi.exception.APIException;
import com.makotora.cardcostapi.exception.cardinfo.InvalidCardNumberException;
import com.makotora.cardcostapi.exception.countryclearingcost.CountryClearingCostNotFoundException;
import com.makotora.cardcostapi.exception.countryclearingcost.InvalidCountryCodeException;

public interface CardClearingCostService
{
    CardClearingCostResponse determineClearingCost(@NonNull String cardNumber)
        throws InvalidCardNumberException, CountryClearingCostNotFoundException, InvalidCountryCodeException,
        APIException;
}
