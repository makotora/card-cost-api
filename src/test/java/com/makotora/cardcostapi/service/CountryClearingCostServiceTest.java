package com.makotora.cardcostapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.makotora.cardcostapi.dao.CountryClearingCostDAO;
import com.makotora.cardcostapi.dto.CountryClearingCostDTO;
import com.makotora.cardcostapi.enums.ErrorCode;
import com.makotora.cardcostapi.exception.countryclearingcost.CountryClearingCostNotFoundException;
import com.makotora.cardcostapi.exception.countryclearingcost.InvalidClearingCostException;
import com.makotora.cardcostapi.exception.countryclearingcost.InvalidCountryCodeException;
import com.makotora.cardcostapi.service.impl.CountryClearingCostServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CountryClearingCostServiceTest
{
    @Spy
    @InjectMocks
    private CountryClearingCostServiceImpl countryClearingCostService;

    @Mock
    private CountryClearingCostDAO countryClearingCostDAO;

    @Test
    public void validateCountryCode_success()
        throws InvalidCountryCodeException
    {
        countryClearingCostService.validateCountryCode("GR");

    }

    @Test
    public void validateCountryCode_invalidCountryLength()
        throws CountryClearingCostNotFoundException, InvalidCountryCodeException
    {
        var actualEx = assertThrows(InvalidCountryCodeException.class, () -> {
            countryClearingCostService.validateCountryCode("123");
        });

        assertEquals(ErrorCode.INVALID_COUNTRY_CODE, actualEx.getErrorCode());
    }

    @Test
    public void validateCountryCode_nullCountry()
        throws CountryClearingCostNotFoundException, InvalidCountryCodeException
    {
        var actualEx = assertThrows(InvalidCountryCodeException.class, () -> {
            countryClearingCostService.validateCountryCode("123");
        });

        assertEquals(ErrorCode.INVALID_COUNTRY_CODE, actualEx.getErrorCode());
    }

    @Test
    public void validateCost_success()
        throws InvalidClearingCostException
    {
        countryClearingCostService.validateCost(BigDecimal.valueOf(12.34));
    }

    @Test
    public void validateCost_negativeCost()
        throws CountryClearingCostNotFoundException, InvalidCountryCodeException
    {
        var actualEx = assertThrows(InvalidClearingCostException.class, () -> {
            countryClearingCostService.validateCost(BigDecimal.valueOf(-1));
        });

        assertEquals(ErrorCode.INVALID_CLEARING_COST, actualEx.getErrorCode());
    }

    @Test
    public void validateCost_nullCost()
        throws CountryClearingCostNotFoundException, InvalidCountryCodeException
    {
        var actualEx = assertThrows(InvalidClearingCostException.class, () -> {
            countryClearingCostService.validateCost(null);
        });

        assertEquals(ErrorCode.INVALID_CLEARING_COST, actualEx.getErrorCode());
    }

    @Test
    public void getByCountryCode_success()
        throws CountryClearingCostNotFoundException, InvalidCountryCodeException
    {
        String countryCode = "GR";
        
        CountryClearingCostDTO countryClearingCostDTO = new CountryClearingCostDTO();
        countryClearingCostDTO.setCountryCode(countryCode);
        countryClearingCostDTO.setCost(BigDecimal.valueOf(12.34));
        
        when(countryClearingCostDAO.findById(countryCode)).thenReturn(Optional.of(countryClearingCostDTO));
        
        CountryClearingCostDTO result = countryClearingCostService.getByCountryCode(countryCode);
        assertEquals(countryClearingCostDTO, result);
    }

    @Test
    public void getByCountryCode_notExists()
        throws CountryClearingCostNotFoundException, InvalidCountryCodeException
    {
        String countryCode = "GR";

        when(countryClearingCostDAO.findById(countryCode)).thenReturn(Optional.empty());

        var actualEx = assertThrows(CountryClearingCostNotFoundException.class, () -> {
            countryClearingCostService.getByCountryCode(countryCode);
        });

        assertEquals(ErrorCode.COUNTRY_CLEARING_COST_NOT_FOUND, actualEx.getErrorCode());
    }

    @Test
    public void determineClearingCost_success_country_code()
        throws CountryClearingCostNotFoundException, InvalidCountryCodeException
    {
        String countryCode = "GR";

        CountryClearingCostDTO countryClearingCost = new CountryClearingCostDTO();
        countryClearingCost.setCountryCode(countryCode);
        countryClearingCost.setCost(BigDecimal.valueOf(12.34));

        when(countryClearingCostService.findByCountryCode(countryCode)).thenReturn(Optional.of(countryClearingCost));

        BigDecimal result = countryClearingCostService.determineClearingCost(countryCode);

        assertEquals(countryClearingCost.getCost(), result);
    }

    @Test
    public void determineClearingCost_success_default()
        throws CountryClearingCostNotFoundException, InvalidCountryCodeException
    {
        String countryCode = "GR";

        when(countryClearingCostDAO.findById(countryCode)).thenReturn(Optional.empty());

        CountryClearingCostDTO defaultClearingCost = new CountryClearingCostDTO();
        defaultClearingCost.setCountryCode(countryCode);
        defaultClearingCost.setCost(BigDecimal.valueOf(12.34));

        when(countryClearingCostService.findByCountryCode(countryCode)).thenReturn(Optional.empty());
        when(countryClearingCostService.findDefaultClearingCost()).thenReturn(Optional.of(defaultClearingCost));

        BigDecimal result = countryClearingCostService.determineClearingCost(countryCode);

        assertEquals(defaultClearingCost.getCost(), result);
    }

    @Test
    public void determineClearingCost_not_found()
        throws CountryClearingCostNotFoundException, InvalidCountryCodeException
    {
        String countryCode = "GR";

        when(countryClearingCostDAO.findById(countryCode)).thenReturn(Optional.empty());

        when(countryClearingCostService.findByCountryCode(countryCode)).thenReturn(Optional.empty());
        when(countryClearingCostService.findDefaultClearingCost()).thenReturn(Optional.empty());

        var actualEx = assertThrows(CountryClearingCostNotFoundException.class, () -> {
            countryClearingCostService.determineClearingCost(countryCode);
        });

        assertEquals(ErrorCode.COUNTRY_CLEARING_COST_NOT_FOUND, actualEx.getErrorCode());
    }

}
