package com.cartisan.charteredPriceCalculator;


import java.util.Date;
import java.util.List;

public interface BaseSetting {
    int carFee(String vehicleType, String startScope, String endScope);

    int driverServiceFee();

    int driverAloneFee(String vehicleType, String driverHomeScope, String targetScope);

    int hotelFee(String driverHomeScope, String targetScope);

    int discount(int days);

    boolean isHotSeason(Date date);

    int hotSeasonHotelFee(String driverHomeScope, String targetScope);

    int hotSeasonServiceRate();

    List<String> driverHomeScopes();

}
