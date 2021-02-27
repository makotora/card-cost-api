package com.makotora.cardcostapi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.makotora.cardcostapi.exception.APIException;
import com.makotora.cardcostapi.exception.cardinfo.InvalidCardNumberException;
import com.makotora.cardcostapi.service.CardInfoService;
import com.makotora.cardcostapi.service.IINCountryCodeProviderService;

@Service
public class CardInfoServiceImpl implements CardInfoService
{
    private final Logger log;

    private final IINCountryCodeProviderService iinCountryCodeProvider;

    @Autowired
    public CardInfoServiceImpl(IINCountryCodeProviderService iinCountryCodeProvider)
    {
        log = LoggerFactory.getLogger(CardInfoServiceImpl.class);
        this.iinCountryCodeProvider = iinCountryCodeProvider;
    }

    @Override
    public String getCountryCode(@NonNull String cardNumber)
        throws InvalidCardNumberException, APIException
    {
        log.debug("getCountryCode [cardNumber={}]", cardNumber);

        validateCardNumber(cardNumber);

        final String issuerIdNumber = getIssuerIdNumber(cardNumber);

        return iinCountryCodeProvider.getCountryCode(issuerIdNumber);
    }

    public void validateCardNumber(@NonNull String cardNumber)
        throws InvalidCardNumberException
    {
        if (cardNumber == null) {
            throw new InvalidCardNumberException("No card number provided.");
        }

        final String cardNumberNoWhitespace = removeAllWhitespace(cardNumber);

        if (!cardNumberNoWhitespace.matches("[0-9]*")) {
            throw new InvalidCardNumberException("Invalid card number. Card numbers must only contain digits.");
        }

        if (cardNumberNoWhitespace.length() != 16) {
            throw new InvalidCardNumberException("Invalid card number length. Card numbers must be 16 digits long.");
        }
    }

    public String getIssuerIdNumber(String cardNumber)
    {
        /* The first 6 to 8 digits of a card number represent
         * the Issuer Identification Number (IIN)
        */
        return removeAllWhitespace(cardNumber).substring(0, 8);
    }

    private String removeAllWhitespace(String str)
    {
        return str.replaceAll("\\s+", "");
    }
}
