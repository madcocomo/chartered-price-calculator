package com.cartisan.charteredPriceCalculator;

import com.cartisan.charteredPriceCalculator.dto.CharteredPriceParam;
import com.cartisan.charteredPriceCalculator.dto.CharteredPriceResult;
import com.cartisan.charteredPriceCalculator.dto.DayTripResult;
import com.cartisan.charteredPriceCalculator.dto.TripPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by colin on 2016/12/17.
 */
public class CharteredCalculator {
    private BaseSetting baseSetting;
    private CharteredPriceSettingService charteredPriceSettingService;

    public CharteredCalculator(CharteredPriceSettingService charteredPriceSettingService) {
        this.charteredPriceSettingService = charteredPriceSettingService;
    }

    public List<CharteredPriceResult> calculate(CharteredPriceParam priceParam) {
        String area = priceParam.getArea();

        if (baseSetting==null){
            baseSetting = new BaseSettingImpl(charteredPriceSettingService, area);
        }

        int discount = baseSetting.discount(priceParam.getTrips().size());

        String vechile = priceParam.getVehicle();

        List<CharteredPriceResult> results = new ArrayList<>();

        List<String> driverHomeScopes = baseSetting.driverHomeScopes();

        for (String driverHomeScope :driverHomeScopes) {


            int totalCarFee = 0;
            int totalServiceFee = 0;
            int totalDriverAloneFee = 0;
            int totalHotelFee = 0;

            List<DayTripResult> trips = new ArrayList<>();
            for (int i = 0; i < priceParam.getTrips().size(); i++) {
                boolean isFirstDay = i==0;
                boolean isLastDay = i==priceParam.getTrips().size()-1;
                TripPath tripPath = priceParam.getTrips().get(i);

                DayTrip dayTrip = new DayTrip(area, tripPath.getTripDate(),vechile,driverHomeScope,
                        discount,tripPath.getPath(), isFirstDay, isLastDay, baseSetting);

                DayTripResult dayTripResult = dayTrip.calculate();

                totalCarFee+=dayTripResult.getCarFee();
                totalServiceFee+=dayTripResult.getServiceFee();
                totalDriverAloneFee+=dayTripResult.getDriverAloneFee();
                totalHotelFee+=dayTripResult.getHotelFee();

                trips.add(dayTripResult);
            }



            int total = totalCarFee+totalServiceFee+totalDriverAloneFee+totalHotelFee;
            CharteredPriceResult result = new CharteredPriceResult(area, vechile, driverHomeScope, discount,
                    totalCarFee, totalServiceFee, totalDriverAloneFee, totalHotelFee,
                    total, trips);


            results.add(result);
        }



        return results;
    }
}
