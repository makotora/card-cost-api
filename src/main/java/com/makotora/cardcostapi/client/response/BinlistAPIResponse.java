package com.makotora.cardcostapi.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/** A class that represents the response received from the BINLIST API.
 * Includes only the attributes that are actually useful to us to avoid unnecessary validation.
 */
public class BinlistAPIResponse
{
    @JsonProperty("country")
    private Country country;

    public BinlistAPIResponse()
    {}

    public Country getCountry()
    {
        return country;
    }

    public void setCountry(Country country)
    {
        this.country = country;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Response [country=");
        builder.append(country);
        builder.append("]");
        return builder.toString();
    }

    public class Country
    {
        @JsonProperty("alpha2")
        private String code;

        public Country()
        {}

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append("Country [code=");
            builder.append(code);
            builder.append("]");
            return builder.toString();
        }

    }

}
