package com.makotora.cardcostapi.exception.countryclearingcost;

import com.makotora.cardcostapi.enums.ErrorCode;
import com.makotora.cardcostapi.exception.AlreadyExistsAPIException;

public class CountryClearingCostAlreadyExistsException extends AlreadyExistsAPIException
{
    private static final long serialVersionUID = 1L;

    public CountryClearingCostAlreadyExistsException(String message)
    {
        super(ErrorCode.COUNTRY_CLEARING_COST_ALREADY_EXISTS, message);
    }

}
