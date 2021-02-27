package com.makotora.cardcostapi.exception.binlistclient;

import com.makotora.cardcostapi.enums.ErrorCode;
import com.makotora.cardcostapi.exception.NotFoundAPIException;

public class BinlistClientCountryCodeNotFoundException extends NotFoundAPIException
{
    private static final long serialVersionUID = 1L;

    public BinlistClientCountryCodeNotFoundException(String message)
    {
        super(ErrorCode.BINLIST_CLIENT_COUNTRY_CODE_NOT_FOUND, message);
    }

}
