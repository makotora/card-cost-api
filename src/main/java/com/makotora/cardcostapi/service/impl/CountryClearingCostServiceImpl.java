package com.makotora.cardcostapi.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.makotora.cardcostapi.dao.CountryClearingCostDAO;
import com.makotora.cardcostapi.dto.CountryClearingCostDTO;
import com.makotora.cardcostapi.exception.countryclearingcost.CountryClearingCostAlreadyExistsException;
import com.makotora.cardcostapi.exception.countryclearingcost.CountryClearingCostNotFoundException;
import com.makotora.cardcostapi.exception.countryclearingcost.InvalidClearingCostException;
import com.makotora.cardcostapi.exception.countryclearingcost.InvalidCountryCodeException;
import com.makotora.cardcostapi.service.CountryClearingCostService;

@Service
public class CountryClearingCostServiceImpl implements CountryClearingCostService
{
    private final Logger log;

    private final String DEFAULT_CLR_COST_COUNTRY_CODE;

    private final CountryClearingCostDAO countryClearingCostDAO;

    @Autowired
    public CountryClearingCostServiceImpl(CountryClearingCostDAO countryClearingCostDAO)
    {
        this.log = LoggerFactory.getLogger(CountryClearingCostServiceImpl.class);
        this.DEFAULT_CLR_COST_COUNTRY_CODE = "Other";
        this.countryClearingCostDAO = countryClearingCostDAO;
    }

    public Optional<CountryClearingCostDTO> findByCountryCode(@NonNull String countryCode)
    {
        return countryClearingCostDAO.findById(countryCode);
    }

    public Optional<CountryClearingCostDTO> findDefaultClearingCost()
        throws InvalidCountryCodeException
    {
        log.debug("findDefaultClearingCost");

        return findByCountryCode(DEFAULT_CLR_COST_COUNTRY_CODE);
    }

    public boolean existsByCountryCode(@NonNull String countryCode)
    {
        return findByCountryCode(countryCode).isPresent();
    }

    @Override
    public List<CountryClearingCostDTO> findAll()
    {
        return countryClearingCostDAO.findAll();
    }

    @Override
    public @NonNull CountryClearingCostDTO getByCountryCode(@NonNull String countryCode)
        throws CountryClearingCostNotFoundException, InvalidCountryCodeException
    {
        log.debug("getByCountryCode [countryCode={}]", countryCode);

        validateCountryCode(countryCode);

        Optional<CountryClearingCostDTO> countryClearingCost = findByCountryCode(countryCode);

        if (!countryClearingCost.isPresent()) {
            throw new CountryClearingCostNotFoundException("No clearing cost defined for the country code '"
                                                           + countryCode
                                                           + "'.");
        }

        return countryClearingCost.get();
    }

    @Override
    public @NonNull BigDecimal determineClearingCost(@NonNull String countryCode)
        throws CountryClearingCostNotFoundException, InvalidCountryCodeException
    {
        log.debug("determineClearingCost [countryCode={}]", countryCode);

        validateCountryCode(countryCode);

        Optional<CountryClearingCostDTO> countryClearingCost = findByCountryCode(countryCode);
        if (countryClearingCost.isPresent()) {
            return countryClearingCost.get().getCost();
        }

        log.info("No clearing cost defined for country code {}. Looking for the default clearing cost.",
                 countryCode);

        Optional<CountryClearingCostDTO> defaultClearingCost = findDefaultClearingCost();
        if (defaultClearingCost.isPresent()) {
            return defaultClearingCost.get().getCost();
        }

        throw new CountryClearingCostNotFoundException("No clearing cost defined for the country code '"
                                                       + countryCode
                                                       + "' and no default clearing cost to use.");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CountryClearingCostDTO create(@NonNull String countryCode, @NonNull BigDecimal cost)
        throws InvalidCountryCodeException, InvalidClearingCostException,
        CountryClearingCostAlreadyExistsException
    {
        log.debug("create [countryCode={}, cost={}]", countryCode, cost);

        validateCountryCode(countryCode);
        validateCost(cost);

        if (existsByCountryCode(countryCode)) {
            throw new CountryClearingCostAlreadyExistsException("A clearing cost is already defined for the country code '"
                                                                + countryCode
                                                                + "'.");
        }

        CountryClearingCostDTO countryClearingCost = new CountryClearingCostDTO();
        countryClearingCost.setCountryCode(countryCode);
        countryClearingCost.setCost(cost);

        countryClearingCostDAO.save(countryClearingCost);

        return countryClearingCost;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CountryClearingCostDTO update(@NonNull String countryCode, @NonNull BigDecimal cost)
        throws InvalidCountryCodeException, InvalidClearingCostException, CountryClearingCostNotFoundException
    {
        log.debug("update [countryCode={}, cost={}]", countryCode, cost);

        validateCountryCode(countryCode);
        validateCost(cost);

        CountryClearingCostDTO countryClearingCost = getByCountryCode(countryCode);

        countryClearingCost.setCost(cost);
        countryClearingCostDAO.save(countryClearingCost);

        return countryClearingCost;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(@NonNull String countryCode)
        throws InvalidCountryCodeException, CountryClearingCostNotFoundException
    {
        log.debug("delete [countryCode={}]", countryCode);

        validateCountryCode(countryCode);

        CountryClearingCostDTO countryClearingCost = getByCountryCode(countryCode);

        countryClearingCostDAO.delete(countryClearingCost);
    }

    public void validateCountryCode(String countryCode)
        throws InvalidCountryCodeException
    {
        if (countryCode == null) {
            throw new InvalidCountryCodeException("No country code provided.");
        }

        if (countryCode.length() != 2 && !countryCode.equals(DEFAULT_CLR_COST_COUNTRY_CODE)) {
            throw new InvalidCountryCodeException("'"
                                                  + countryCode
                                                  + "' is not a valid ISO 3166 alpha-2 code and is not the default country code '"
                                                  + DEFAULT_CLR_COST_COUNTRY_CODE
                                                  + "'.");
        }
    }

    public void validateCost(BigDecimal cost)
        throws InvalidClearingCostException
    {
        if (cost == null) {
            throw new InvalidClearingCostException("No clearing cost provided.");
        }

        if (cost.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidClearingCostException("The clearing cost cannot be negative.");
        }
    }

}
