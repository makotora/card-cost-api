package com.makotora.cardcostapi.exception;

import com.makotora.cardcostapi.enums.ErrorCode;

public class InvalidArgumentAPIException extends APIException
{
    private static final long serialVersionUID = 1L;

    public InvalidArgumentAPIException(ErrorCode errorCode, String message)
    {
        super(errorCode, message);
    }

    public InvalidArgumentAPIException(ErrorCode errorCode, String message, Throwable cause)
    {
        super(errorCode, message, cause);
    }

}
