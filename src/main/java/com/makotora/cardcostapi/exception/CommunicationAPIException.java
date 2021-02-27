package com.makotora.cardcostapi.exception;

import com.makotora.cardcostapi.enums.ErrorCode;

public class CommunicationAPIException extends APIException
{
    private static final long serialVersionUID = 1L;

    public CommunicationAPIException(ErrorCode errorCode, String message)
    {
        super(errorCode, message);
    }

    public CommunicationAPIException(ErrorCode errorCode, String message, Throwable cause)
    {
        super(errorCode, message, cause);
    }

}
