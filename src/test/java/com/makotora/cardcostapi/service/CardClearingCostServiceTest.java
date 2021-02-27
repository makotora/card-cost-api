package com.makotora.cardcostapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.makotora.cardcostapi.controller.response.CardClearingCostResponse;
import com.makotora.cardcostapi.exception.APIException;
import com.makotora.cardcostapi.exception.cardinfo.InvalidCardNumberException;
import com.makotora.cardcostapi.service.impl.CardClearingCostServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CardClearingCostServiceTest
{
    @InjectMocks
    private CardClearingCostServiceImpl cardClearingCostService;

    @Mock
    private CardInfoService cardInfoService;

    @Mock
    private CountryClearingCostService countryClearingCostService;

    @Test
    public void determineClearingCost_success()
        throws InvalidCardNumberException, APIException
    {
        String cardNumber = "1234 1234 1234 1234";
        String countryCode = "GR";
        BigDecimal cost = BigDecimal.valueOf(12.34);

        when(cardInfoService.getCountryCode(cardNumber)).thenReturn(countryCode);
        when(countryClearingCostService.determineClearingCost(countryCode)).thenReturn(cost);

        CardClearingCostServiceImpl cardClearingCostService = new CardClearingCostServiceImpl(cardInfoService,
                                                                                              countryClearingCostService);

        CardClearingCostResponse result = cardClearingCostService.determineClearingCost(cardNumber);

        assertEquals(new CardClearingCostResponse(countryCode, cost), result);
    }
}
