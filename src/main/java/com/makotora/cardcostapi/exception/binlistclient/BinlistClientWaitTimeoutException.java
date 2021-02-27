package com.makotora.cardcostapi.exception.binlistclient;

import com.makotora.cardcostapi.enums.ErrorCode;
import com.makotora.cardcostapi.exception.WaitTimeoutAPIException;

public class BinlistClientWaitTimeoutException extends WaitTimeoutAPIException
{
    private static final long serialVersionUID = 1L;

    public BinlistClientWaitTimeoutException(String message)
    {
        super(ErrorCode.BINLIST_CLIENT_WAIT_TIMEOUT, message);
    }

}
