package com.cartisan.charteredPriceCalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CharteredPriceResult{
    private String area;
    private String vehicle;
    private String driverHomeScope;
    private int discount;

    private int totalCarFee;
    private int totalServiceFee;
    private int totalDriverAloneFee;
    private int totalHotelFee;

    private int total;

    private List<DayTripResult> trips;

}