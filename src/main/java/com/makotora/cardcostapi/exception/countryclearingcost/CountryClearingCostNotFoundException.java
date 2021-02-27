package com.makotora.cardcostapi.exception.countryclearingcost;

import com.makotora.cardcostapi.enums.ErrorCode;
import com.makotora.cardcostapi.exception.NotFoundAPIException;

public class CountryClearingCostNotFoundException extends NotFoundAPIException
{
    private static final long serialVersionUID = 1L;

    public CountryClearingCostNotFoundException(String message)
    {
        super(ErrorCode.COUNTRY_CLEARING_COST_NOT_FOUND, message);
    }

}
