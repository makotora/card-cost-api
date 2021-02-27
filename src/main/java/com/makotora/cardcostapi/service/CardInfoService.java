package com.makotora.cardcostapi.service;

import org.springframework.lang.NonNull;

import com.makotora.cardcostapi.exception.APIException;
import com.makotora.cardcostapi.exception.cardinfo.InvalidCardNumberException;

public interface CardInfoService
{
    String getCountryCode(@NonNull String cardNumber)
        throws InvalidCardNumberException, APIException;
}
