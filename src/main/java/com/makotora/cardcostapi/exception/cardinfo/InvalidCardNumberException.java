package com.makotora.cardcostapi.exception.cardinfo;

import com.makotora.cardcostapi.enums.ErrorCode;
import com.makotora.cardcostapi.exception.InvalidArgumentAPIException;

public class InvalidCardNumberException extends InvalidArgumentAPIException
{
    private static final long serialVersionUID = 1L;

    public InvalidCardNumberException(String message)
    {
        super(ErrorCode.INVALID_CARD_NUMBER, message);
    }
}
