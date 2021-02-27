package com.makotora.cardcostapi.enums;

public enum ErrorCode
{
    // CountryClearingCostService
    COUNTRY_CLEARING_COST_NOT_FOUND("CCC_01"),
    COUNTRY_CLEARING_COST_ALREADY_EXISTS("CCC_02"),
    INVALID_COUNTRY_CODE("CCC_03"),
    INVALID_CLEARING_COST("CCC_04"),
    
    // CardInfoService
    INVALID_CARD_NUMBER("CI_01"),

    // BinlistAPIClient
    BINLIST_CLIENT_COUNTRY_CODE_NOT_FOUND("BLC_01"),
    BINLIST_CLIENT_REQUEST_ERROR("BLC_02"),
    BINLIST_CLIENT_WAIT_TIMEOUT("BLC_03");

    public final String code;

    private ErrorCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
}
