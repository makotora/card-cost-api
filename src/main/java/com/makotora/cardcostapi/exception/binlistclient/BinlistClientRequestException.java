package com.makotora.cardcostapi.exception.binlistclient;

import com.makotora.cardcostapi.enums.ErrorCode;
import com.makotora.cardcostapi.exception.CommunicationAPIException;

public class BinlistClientRequestException extends CommunicationAPIException
{
    private static final long serialVersionUID = 1L;

    public BinlistClientRequestException(String message, Throwable cause)
    {
        super(ErrorCode.BINLIST_CLIENT_REQUEST_ERROR, message, cause);
    }

}
