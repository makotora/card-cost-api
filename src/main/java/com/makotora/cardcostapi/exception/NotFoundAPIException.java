package com.makotora.cardcostapi.exception;

import com.makotora.cardcostapi.enums.ErrorCode;

public class NotFoundAPIException extends APIException
{
    private static final long serialVersionUID = 1L;

    public NotFoundAPIException(ErrorCode errorCode, String message)
    {
        super(errorCode, message);
    }

    public NotFoundAPIException(ErrorCode errorCode, String message, Throwable cause)
    {
        super(errorCode, message, cause);
    }

}
