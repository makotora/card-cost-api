package com.makotora.cardcostapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.makotora.cardcostapi.constants.APIPaths;
import com.makotora.cardcostapi.controller.request.CountryClearingCostCreateRequest;
import com.makotora.cardcostapi.controller.request.CountryClearingCostUpdateRequest;
import com.makotora.cardcostapi.dto.CountryClearingCostDTO;
import com.makotora.cardcostapi.exception.APIException;
import com.makotora.cardcostapi.service.CountryClearingCostService;

@RestController
public class CountryClearingCostController
{
    public final Logger log;

    private final CountryClearingCostService countryClearingCostService;

    @Autowired
    public CountryClearingCostController(CountryClearingCostService countryClearingCostService)
    {
        this.log = LoggerFactory.getLogger(CountryClearingCostController.class);
        this.countryClearingCostService = countryClearingCostService;
    }

    @RequestMapping(path = APIPaths.COUNTRY_CLEARING_COST_PATH, method = RequestMethod.GET)
    public List<CountryClearingCostDTO> viewAll()
    {
        log.debug("viewAll");

        return countryClearingCostService.findAll();
    }

    @RequestMapping(path = APIPaths.COUNTRY_CLEARING_COST_PATH + "/{countryCode}", method = RequestMethod.GET)
    public CountryClearingCostDTO view(@PathVariable String countryCode)
        throws APIException
    {
        log.debug("view [countryCode={}]", countryCode);

        return countryClearingCostService.getByCountryCode(countryCode);
    }

    @RequestMapping(path = APIPaths.COUNTRY_CLEARING_COST_PATH, method = RequestMethod.POST)
    public CountryClearingCostDTO create(
        @RequestBody CountryClearingCostCreateRequest countryClrCostCreateRequest)
        throws APIException
    {
        log.debug("create [countryClrCostCreateRequest={}]", countryClrCostCreateRequest);

        return countryClearingCostService.create(countryClrCostCreateRequest.getCountryCode(),
                                                 countryClrCostCreateRequest.getCost());
    }

    @RequestMapping(path = APIPaths.COUNTRY_CLEARING_COST_PATH + "/{countryCode}", method = RequestMethod.PUT)
    public CountryClearingCostDTO update(
        @PathVariable String countryCode,
        @RequestBody CountryClearingCostUpdateRequest countryClrCostUpdateRequest)
        throws APIException
    {
        log.debug("update [countryCode={}, countryClrCostUpdateRequest={}]",
                  countryCode,
                  countryClrCostUpdateRequest);

        return countryClearingCostService.update(countryCode, countryClrCostUpdateRequest.getCost());
    }

    @RequestMapping(
        path = APIPaths.COUNTRY_CLEARING_COST_PATH + "/{countryCode}",
        method = RequestMethod.DELETE)
    public void delete(@PathVariable String countryCode)
        throws APIException
    {
        log.debug("delete [countryCode={}]", countryCode);

        countryClearingCostService.delete(countryCode);
    }
}
