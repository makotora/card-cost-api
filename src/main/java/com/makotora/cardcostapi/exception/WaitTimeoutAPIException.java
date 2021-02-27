package com.makotora.cardcostapi.exception;

import com.makotora.cardcostapi.enums.ErrorCode;

public class WaitTimeoutAPIException extends APIException
{
    private static final long serialVersionUID = 1L;

    public WaitTimeoutAPIException(ErrorCode errorCode, String message)
    {
        super(errorCode, message);
    }

    public WaitTimeoutAPIException(ErrorCode errorCode, String message, Throwable cause)
    {
        super(errorCode, message, cause);
    }

}
