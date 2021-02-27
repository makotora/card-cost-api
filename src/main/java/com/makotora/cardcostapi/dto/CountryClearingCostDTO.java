package com.makotora.cardcostapi.dto;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.makotora.cardcostapi.constants.AppConstants;

/** Class that represents the clearing cost for a specific country. 
 */
@Entity
@Table(name = CountryClearingCostDTO.TABLE_NAME)
public class CountryClearingCostDTO
{
    public static final String TABLE_NAME = AppConstants.TABLE_NAME_PREFIX + "COUNTRY_CLR_COST";

    public static final String COLUMN_NAME_COUNTRY_CODE = "COUNTRY_CODE";
    public static final String COLUMN_NAME_CLEARING_COST = "COST";

    /** The country code following the ISO 3166 alpha-2 international standard.
     */
    @Id
    @Column(name = COLUMN_NAME_COUNTRY_CODE)
    private String countryCode;

    /** The clearing cost in USD.
     */
    @Column(
        name = COLUMN_NAME_CLEARING_COST,
        precision = AppConstants.CURRENCY_PRECISION,
        scale = AppConstants.CURRENCY_SCALE)
    private BigDecimal cost;

    public CountryClearingCostDTO()
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
        CountryClearingCostDTO other = (CountryClearingCostDTO) obj;
        return Objects.equals(cost, other.cost) && Objects.equals(countryCode, other.countryCode);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CountryClearingCostDTO [countryCode=");
        builder.append(countryCode);
        builder.append(", cost=");
        builder.append(cost);
        builder.append("]");
        return builder.toString();
    }
}
