package com.cartisan.charteredPriceCalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class DayTripResult {
    private Date tripDate;
    private List<String> path;
    private boolean isHotSeason;

    private int carFee;
    private int serviceFee;
    private int driverAloneFee;
    private int hotelFee;

    private int total;
}
