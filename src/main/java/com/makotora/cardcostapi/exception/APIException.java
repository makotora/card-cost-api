package com.makotora.cardcostapi.exception;

import com.makotora.cardcostapi.enums.ErrorCode;

public class APIException extends Exception
{
    private static final long serialVersionUID = 1L;

    private final ErrorCode errorCode;

    public APIException(ErrorCode errorCode, String message)
    {
        super(message);
        this.errorCode = errorCode;
    }

    public APIException(ErrorCode errorCode, String message, Throwable cause)
    {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode()
    {
        return errorCode;
    }

}
