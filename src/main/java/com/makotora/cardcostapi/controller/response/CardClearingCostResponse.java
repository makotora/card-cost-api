package com.makotora.cardcostapi.controller.response;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardClearingCostResponse
{
    @JsonProperty(value = "country")
    private String countryCode;

    @JsonProperty(value = "cost")
    private BigDecimal cost;

    public CardClearingCostResponse()
    {}

    public CardClearingCostResponse(String countryCode, BigDecimal cost)
    {
        super();
        this.countryCode = countryCode;
        this.cost = cost;
    }

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
    public int hashCode()
    {
        return Objects.hash(cost, countryCode);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CardClearingCostResponse other = (CardClearingCostResponse) obj;
        return Objects.equals(cost, other.cost) && Objects.equals(countryCode, other.countryCode);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CardClearingCostResponse [countryCode=");
        builder.append(countryCode);
        builder.append(", cost=");
        builder.append(cost);
        builder.append("]");
        return builder.toString();
    }

}
