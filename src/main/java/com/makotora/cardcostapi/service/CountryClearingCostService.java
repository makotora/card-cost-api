package com.makotora.cardcostapi.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.lang.NonNull;

import com.makotora.cardcostapi.dto.CountryClearingCostDTO;
import com.makotora.cardcostapi.exception.countryclearingcost.CountryClearingCostAlreadyExistsException;
import com.makotora.cardcostapi.exception.countryclearingcost.CountryClearingCostNotFoundException;
import com.makotora.cardcostapi.exception.countryclearingcost.InvalidClearingCostException;
import com.makotora.cardcostapi.exception.countryclearingcost.InvalidCountryCodeException;

public interface CountryClearingCostService
{
    List<CountryClearingCostDTO> findAll();

    CountryClearingCostDTO getByCountryCode(@NonNull String countryCode)
        throws CountryClearingCostNotFoundException, InvalidCountryCodeException;

    BigDecimal determineClearingCost(@NonNull String countryCode)
        throws CountryClearingCostNotFoundException, InvalidCountryCodeException;

    CountryClearingCostDTO create(@NonNull String countryCode, @NonNull BigDecimal cost)
        throws CountryClearingCostAlreadyExistsException, InvalidCountryCodeException,
        InvalidClearingCostException;

    CountryClearingCostDTO update(@NonNull String countryCode, @NonNull BigDecimal cost)
        throws InvalidCountryCodeException, InvalidClearingCostException,
        CountryClearingCostNotFoundException;

    void delete(@NonNull String countryCode)
        throws InvalidCountryCodeException, CountryClearingCostNotFoundException;

}
