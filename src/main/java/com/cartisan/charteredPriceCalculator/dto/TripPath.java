package com.cartisan.charteredPriceCalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class TripPath {
    private Date tripDate;
    private List<String> path;
}
