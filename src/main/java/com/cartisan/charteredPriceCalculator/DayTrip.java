package com.cartisan.charteredPriceCalculator;

import com.cartisan.charteredPriceCalculator.dto.DayTripResult;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class DayTrip {
    private String area;
    private Date tripDate;
    private String vehicle;
    private String driverHomeScope;
    private int discount;
    private List<String> path;

    private boolean isFirstDay;
    private boolean isLastDay;

    private BaseSetting baseSetting;


    @Override
    public boolean equals(Object other) {
        DayTrip right =(DayTrip)other;
        boolean success = this.area.equals(right.area) &&
                this.tripDate.equals(right.tripDate) &&
                this.vehicle.equals(right.vehicle) &&
                this.driverHomeScope.equals(right.driverHomeScope) &&
                this.path.equals(right.path) &&
                this.isLastDay == right.isLastDay;
        return success;
    }

    public DayTripResult calculate(){
        boolean isHotSeason = baseSetting.isHotSeason(tripDate);

        int carFee = calculateCarFee();
        int serviceFee = calculateServiceFee();
        int hotelFee = calculateHotelFee(isHotSeason);
        int driverAloneFee = calculateDriverAloneFee();

        int total=0;
        if (isHotSeason){
            total=(carFee + serviceFee + driverAloneFee)*(100+baseSetting.hotSeasonServiceRate())/100+hotelFee;
        }
        else {
            total = carFee + serviceFee + hotelFee + driverAloneFee;
        }
        DayTripResult result = new DayTripResult(tripDate, path, isHotSeason,
                carFee, serviceFee, hotelFee, driverAloneFee, total );


        return result;
    }

    private int calculateDriverAloneFee() {
        int driverAloneFee = 0;

        if (isFirstDay){
            driverAloneFee = baseSetting.driverAloneFee(vehicle,
                    driverHomeScope, path.get(0));
        }

        if (isLastDay){
            driverAloneFee+=baseSetting.driverAloneFee(vehicle,
                    driverHomeScope, path.get(path.size()-1));
        }
        return driverAloneFee;
    }

    private int calculateHotelFee(boolean isHotSeason) {
        int hotelFee = 0;
        if(!isLastDay) {
            if (isHotSeason)
                hotelFee = baseSetting.hotSeasonHotelFee(driverHomeScope,
                        path.get(path.size()-1));
            else
                hotelFee = baseSetting.hotelFee(driverHomeScope,
                        path.get(path.size()-1));
        }
        return hotelFee;
    }

    private int calculateServiceFee() {
        int serviceFee = baseSetting.driverServiceFee();

        return serviceFee * discount / 100;
    }


    private int calculateCarFee() {
        int vehicleTypeDayPrice = 0;
        for(int i = 0; i < path.size()-1; i++) {
            vehicleTypeDayPrice += baseSetting.carFee(vehicle, path.get(i), path.get(i+1));
        }

        return vehicleTypeDayPrice;
    }
}
