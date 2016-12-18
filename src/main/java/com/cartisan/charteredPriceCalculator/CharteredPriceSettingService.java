package com.cartisan.charteredPriceCalculator;

import com.cartisan.charteredPriceCalculator.dto.Discount;
import com.cartisan.charteredPriceCalculator.dto.HotSeason;
import com.cartisan.charteredPriceCalculator.dto.PriceSetting;

import java.util.List;
public interface CharteredPriceSettingService {
    List<PriceSetting> getPriceSettings(String area);

    int getDriverServiceFee(String area);

    int getDriverAloneFee(String area);

    int getDriverHotelFee(String area);

    List<Discount> getDiscounts(String area);

    HotSeason getHotSeason(String area);

    List<String> getDriverHomeScopes(String area);
}
