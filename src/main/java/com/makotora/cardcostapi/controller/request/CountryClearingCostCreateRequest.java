package com.makotora.cardcostapi.controller.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class CountryClearingCostCreateRequest implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String countryCode;

    private BigDecimal cost;

    public CountryClearingCostCreateRequest()
    {}

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public BigDecimal getCost()
    {
        return cost;
    }

    public void setCost(BigDecimal cost)
    {
        this.cost = cost;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CountryClearingCostCreateRequest [countryCode=");
        builder.append(countryCode);
        builder.append(", cost=");
        builder.append(cost);
        builder.append("]");
        return builder.toString();
    }

}
