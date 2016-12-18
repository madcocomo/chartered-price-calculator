package com.cartisan.charteredPriceCalculator.dto;

import lombok.Data;

@Data
public class PriceScopeSetting{
    private String start;
    private String end;
    private int price;
    private int hours;
}
