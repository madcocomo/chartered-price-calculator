package com.cartisan.charteredPriceCalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CharteredPriceParam {
    private String area;
    private String vehicle;

    private List<TripPath> trips;

    public CharteredPriceParam() {
    }
}
