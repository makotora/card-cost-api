package com.makotora.cardcostapi.controller.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardClearingCostRequest implements Serializable
{
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "card_number")
    private String cardNumber;

    public CardClearingCostRequest()
    {}

    public String getCardNumber()
    {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber)
    {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CardClearingCostRequest [cardNumber=");
        builder.append(cardNumber);
        builder.append("]");
        return builder.toString();
    }

}
