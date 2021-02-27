package com.makotora.cardcostapi.exception;

import com.makotora.cardcostapi.enums.ErrorCode;

public class AlreadyExistsAPIException extends APIException
{
    private static final long serialVersionUID = 1L;

    public AlreadyExistsAPIException(ErrorCode errorCode, String message)
    {
        super(errorCode, message);
    }

    public AlreadyExistsAPIException(ErrorCode errorCode, String message, Throwable cause)
    {
        super(errorCode, message, cause);
    }

}
