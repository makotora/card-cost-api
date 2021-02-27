package com.makotora.cardcostapi.exception.countryclearingcost;

import com.makotora.cardcostapi.enums.ErrorCode;
import com.makotora.cardcostapi.exception.InvalidArgumentAPIException;

public class InvalidClearingCostException extends InvalidArgumentAPIException
{
    private static final long serialVersionUID = 1L;

    public InvalidClearingCostException(String message)
    {
        super(ErrorCode.INVALID_CLEARING_COST, message);
    }

}
