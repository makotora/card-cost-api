package com.makotora.cardcostapi.exception.countryclearingcost;

import com.makotora.cardcostapi.enums.ErrorCode;
import com.makotora.cardcostapi.exception.InvalidArgumentAPIException;

public class InvalidCountryCodeException extends InvalidArgumentAPIException
{
    private static final long serialVersionUID = 1L;

    public InvalidCountryCodeException(String message)
    {
        super(ErrorCode.INVALID_COUNTRY_CODE, message);
    }

}
