package com.cartisan.charteredPriceCalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Discount{
    private int min;
    private int max;
    private int discount;
}
