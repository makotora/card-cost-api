package com.makotora.cardcostapi.service;

import com.makotora.cardcostapi.exception.APIException;

public interface IINCountryCodeProviderService
{
    String getCountryCode(String issuerIdNumber)
        throws APIException;
}
