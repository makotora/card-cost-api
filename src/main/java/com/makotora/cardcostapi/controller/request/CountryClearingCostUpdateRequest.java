package com.makotora.cardcostapi.controller.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class CountryClearingCostUpdateRequest implements Serializable
{
    private static final long serialVersionUID = 1L;

    private BigDecimal cost;

    public CountryClearingCostUpdateRequest()
    {}

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
        builder.append("CountryClearingCostUpdateRequest [cost=");
        builder.append(cost);
        builder.append("]");
        return builder.toString();
    }

}
