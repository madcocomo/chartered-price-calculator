package com.cartisan.charteredPriceCalculator.dto;

import lombok.Data;

import java.util.List;

@Data
public class PriceSetting  {
    private String vehicle;

    private List<PriceScopeSetting> scopeSettings;
}
