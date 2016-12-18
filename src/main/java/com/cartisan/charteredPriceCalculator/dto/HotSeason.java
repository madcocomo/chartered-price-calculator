package com.cartisan.charteredPriceCalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class HotSeason {
    private Date start;
    private Date end;
    private int rate;
    private int hotelFee;
}
