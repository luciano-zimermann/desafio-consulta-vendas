package com.devsuperior.dsmeta.dto;

import com.devsuperior.dsmeta.projections.SellerAmountProjection;

public class SellerDTO
{
    private String name;
    private Double amount;

    public SellerDTO()
    {
    }

    public SellerDTO( String name, Double amount )
    {
        this.name = name;
        this.amount = amount;
    }

    public SellerDTO( SellerAmountProjection projection )
    {
        name = projection.getName();
        amount = projection.getTotal();
    }

    public String getName()
    {
        return name;
    }

    public Double getAmount()
    {
        return amount;
    }
}
