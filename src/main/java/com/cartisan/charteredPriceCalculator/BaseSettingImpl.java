package com.cartisan.charteredPriceCalculator;


import com.cartisan.charteredPriceCalculator.dto.Discount;
import com.cartisan.charteredPriceCalculator.dto.HotSeason;
import com.cartisan.charteredPriceCalculator.dto.PriceScopeSetting;
import com.cartisan.charteredPriceCalculator.dto.PriceSetting;

import java.util.Date;
import java.util.List;

public class BaseSettingImpl implements BaseSetting {
    private CharteredPriceSettingService priceSettingService;
    private String area;

    public BaseSettingImpl(CharteredPriceSettingService priceSettingService, String area) {
        this.priceSettingService = priceSettingService;
        this.area = area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public int carFee(String vehicleType, String startScope, String endScope) {
        List<PriceSetting> priceSettings = priceSettingService.getPriceSettings(area);

        int price = 0;

        for (PriceSetting priceSetting: priceSettings){
            if (priceSetting.getVehicle().equals(vehicleType)){
                List<PriceScopeSetting> scopeSettings = priceSetting.getScopeSettings();

                for (PriceScopeSetting scopeSetting: scopeSettings){
                    if (scopeSetting.getStart().equals(startScope) && scopeSetting.getEnd().equals(endScope)){
                        price = scopeSetting.getPrice();
                        break;
                    }
                }


                break;
            }
        }

        if (price==0) throw new RuntimeException();
        return price;
    }

    @Override
    public int driverServiceFee() {
        return priceSettingService.getDriverServiceFee(area);
    }

    @Override
    public int driverAloneFee(String vehicleType, String driverHomeScope, String targetScope) {
        int driverServiceFee = priceSettingService.getDriverAloneFee(area);

        List<PriceSetting> priceSettings = priceSettingService.getPriceSettings(area);

        int hours = 0;

        for (PriceSetting priceSetting: priceSettings){
            if (priceSetting.getVehicle().equals(vehicleType)){
                List<PriceScopeSetting> scopeSettings = priceSetting.getScopeSettings();

                for (PriceScopeSetting scopeSetting: scopeSettings){
                    if ((scopeSetting.getStart().equals(driverHomeScope) && scopeSetting.getEnd().equals(targetScope))
                            || (scopeSetting.getEnd().equals(driverHomeScope) && scopeSetting.getStart().equals(targetScope))){
                        hours = scopeSetting.getHours();
                        break;
                    }
                }


                break;
            }
        }

        return driverServiceFee * hours;
    }

    @Override
    public int hotelFee(String driverHomeScope, String targetScope) {
        int result = 0;
        if(!driverHomeScope.equals(targetScope)){
            result = priceSettingService.getDriverHotelFee(area);
        }

        return result;
    }

    @Override
    public int discount(int days) {
        List<Discount> discounts = priceSettingService.getDiscounts("");

        int discountValue = 100;

        for (Discount discount: discounts){
            if (days>=discount.getMin() && days<=discount.getMax()){
                discountValue = discount.getDiscount();
                break;
            }
        }

        return discountValue;
    }

    @Override
    public boolean isHotSeason(Date date) {
        HotSeason hotSeasonSetting = priceSettingService.getHotSeason(area);
        if(hotSeasonSetting.getEnd().compareTo(date) >= 0 && hotSeasonSetting.getStart().compareTo(date)<=0 )
        {
            return true;
        }
        return false ;
    }

    @Override
    public int hotSeasonHotelFee(String driverHomeScope, String targetScope) {
        int result = 0;
        if(!driverHomeScope.equals(targetScope)){
            HotSeason hotSeasonSetting = priceSettingService.getHotSeason(area);
            result = hotSeasonSetting.getHotelFee();
        }

        return result;
    }

    @Override
    public int hotSeasonServiceRate() {
        HotSeason hotSeasonSetting = priceSettingService.getHotSeason(area);
        return hotSeasonSetting.getRate();
    }

    @Override
    public List<String> driverHomeScopes() {
        return priceSettingService.getDriverHomeScopes(area);
    }
}
