package com.makotora.cardcostapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.makotora.cardcostapi.enums.ErrorCode;
import com.makotora.cardcostapi.exception.cardinfo.InvalidCardNumberException;
import com.makotora.cardcostapi.service.impl.CardInfoServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CardInfoServiceTest
{
    @Spy
    @InjectMocks
    CardInfoServiceImpl cardInfoService;

    @Mock
    IINCountryCodeProviderService iinCountryCodeProviderService;

    @Test
    public void validateCardNumber_success()
        throws InvalidCardNumberException
    {
        cardInfoService.validateCardNumber("1234567812345678");
    }

    @Test
    public void validateCardNumber_success_whitespace()
        throws InvalidCardNumberException
    {
        cardInfoService.validateCardNumber("1234 5678 1234 5678");
    }

    @Test
    public void validateCardNumber_null()
        throws InvalidCardNumberException
    {
        var actualEx = assertThrows(InvalidCardNumberException.class, () -> {
            cardInfoService.validateCardNumber(null);
        });

        assertEquals(ErrorCode.INVALID_CARD_NUMBER, actualEx.getErrorCode());
    }

    @Test
    public void validateCardNumber_non_digit()
        throws InvalidCardNumberException
    {
        var actualEx = assertThrows(InvalidCardNumberException.class, () -> {
            cardInfoService.validateCardNumber("XXXX123412341234");
        });

        assertEquals(ErrorCode.INVALID_CARD_NUMBER, actualEx.getErrorCode());
    }

    @Test
    public void validateCardNumber_invalid_length()
        throws InvalidCardNumberException
    {
        var actualEx = assertThrows(InvalidCardNumberException.class, () -> {
            cardInfoService.validateCardNumber("1234 5678 1234 56781");
        });

        assertEquals(ErrorCode.INVALID_CARD_NUMBER, actualEx.getErrorCode());
    }

    @Test
    public void getIssuerIdNumber_success()
        throws InvalidCardNumberException
    {
        String result = cardInfoService.getIssuerIdNumber("1234 5678 1234 56781");

        assertEquals("12345678", result);
    }
}
