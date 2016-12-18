package com.cartisan.charteredPriceCalculator;

import com.cartisan.charteredPriceCalculator.dto.Discount;
import com.cartisan.charteredPriceCalculator.dto.HotSeason;
import com.cartisan.charteredPriceCalculator.dto.PriceScopeSetting;
import com.cartisan.charteredPriceCalculator.dto.PriceSetting;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by colin on 2016/12/17.
 */
public class BaseSettingImplTest {
    @Test
    public  void car_fee_from_A_to_A()
    {
        // Arrange
        CharteredPriceSettingService service = mock(CharteredPriceSettingService.class);

        String setting = "JingJi6:A,A,300,0";
        when(service.getPriceSettings(anyString())).thenReturn(generatePriceSetting(setting));

        BaseSetting baseSetting =new BaseSettingImpl(service, anyString());

        // Act
        int  result=  baseSetting.carFee("JingJi6", "A", "A");

        // Assert
        assertEquals(300,result);

    }

    private List<PriceSetting> generatePriceSetting(String setting){
        String[] vehicleSettings = setting.split(";");
        List<PriceSetting> settings = new ArrayList<>();

        for (String vehicleSetting: vehicleSettings){
            PriceSetting priceSetting = new PriceSetting();
            String[] scopeSettings = vehicleSetting.split(":");
            priceSetting.setVehicle(scopeSettings[0]);

            List<PriceScopeSetting> priceScopeSettings = new ArrayList<>();

            for (int i = 1; i < scopeSettings.length; i++) {
                String[] values = scopeSettings[i].split(",");
                PriceScopeSetting scopeSetting = new PriceScopeSetting();
                scopeSetting.setStart(values[0]);
                scopeSetting.setEnd(values[1]);
                scopeSetting.setPrice(parseInt(values[2]));
                scopeSetting.setHours(parseInt(values[3]));

                priceScopeSettings.add(scopeSetting);
            }

            priceSetting.setScopeSettings(priceScopeSettings);
            settings.add(priceSetting);
        }

        return settings;
    }

    @Test
    public  void car_fee_from_A_to_B()
    {
        // Arrange
        CharteredPriceSettingService service = mock(CharteredPriceSettingService.class);

        String setting = "JingJi6:A,B,300,0";
        when(service.getPriceSettings(anyString())).thenReturn(generatePriceSetting(setting));

        BaseSetting baseSetting =new BaseSettingImpl(service, anyString());

        // Act
        int  result=  baseSetting.carFee("JingJi6", "A", "B");

        // Assert
        assertEquals(300,result);
    }


    @Test(expected = RuntimeException.class)
    public void not_car_fee_setting_then_throw_exception(){
        // Arrange
        CharteredPriceSettingService service = mock(CharteredPriceSettingService.class);

        when(service.getPriceSettings(anyString())).thenReturn(generatePriceSetting(""));

        BaseSetting baseSetting =new BaseSettingImpl(service, anyString());

        // Act
        baseSetting.carFee("JingJi6", "A", "B");
    }

    @Test
    public  void driver_service_fee()
    {
        CharteredPriceSettingService service = mock(CharteredPriceSettingService.class);
        when(service.getDriverServiceFee(anyString())).thenReturn(100);

        BaseSetting baseSetting =new BaseSettingImpl(service, anyString());

        int  result=  baseSetting.driverServiceFee();
        assertEquals(100,result);
    }


    @Test
    public  void driver_alone_fee()
    {
        CharteredPriceSettingService service = mock(CharteredPriceSettingService.class);
        when(service.getDriverAloneFee(anyString())).thenReturn(25);

        String setting = "JingJi6:" +
                "A,A,300,0:" +
                "A,B,400,2";
        when(service.getPriceSettings(anyString())).thenReturn(generatePriceSetting(setting));

        BaseSetting baseSetting =new BaseSettingImpl(service, "");

        int  result1=  baseSetting.driverAloneFee("JingJi6", "A", "A");
        int  result2=  baseSetting.driverAloneFee("JingJi6", "A", "B");
        assertEquals(0,result1);
        assertEquals(50,result2);

    }

    @Test
    public  void driver_hotel_fee()
    {
        CharteredPriceSettingService service = mock(CharteredPriceSettingService.class);
        when(service.getDriverHotelFee(anyString())).thenReturn(200);

        BaseSetting baseSetting =new BaseSettingImpl(service, anyString());

        int  result1=  baseSetting.hotelFee("A", "A");
        int  result2=  baseSetting.hotelFee("A", "B");
        assertEquals(0,result1);
        assertEquals(200,result2);
    }

    @Test
    public void discount()
    {
        CharteredPriceSettingService service = mock(CharteredPriceSettingService.class);

        Discount discount1 = new Discount(2,2,97);
        Discount discount2 = new Discount(3,5,93);
        Discount discount3 = new Discount(6,10,90);
        Discount discount4 = new Discount(11,100,87);

        when(service.getDiscounts(anyString())).thenReturn(asList(discount1, discount2, discount3, discount4));

        BaseSetting baseSetting =new BaseSettingImpl(service, anyString());

        int  result1=  baseSetting.discount(2);
        int  result2=  baseSetting.discount(3);
        int  result3=  baseSetting.discount(5);
        int  result4=  baseSetting.discount(6);
        int  result5=  baseSetting.discount(10);
        int  result6=  baseSetting.discount(11);

        assertEquals(97,result1);
        assertEquals(93,result2);
        assertEquals(93,result3);
        assertEquals(90,result4);
        assertEquals(90,result5);
        assertEquals(87,result6);

    }

    @Test
    public void  is_hot_day()
    {
        CharteredPriceSettingService service = mock(CharteredPriceSettingService.class);
        Calendar start= Calendar.getInstance();
        start.set(2016,5,20);

        Calendar end= Calendar.getInstance();
        end.set(2016,5,30);

        HotSeason hotSeasonSetting = new HotSeason(start.getTime(),end.getTime(),30,500);
        when(service.getHotSeason(anyString())).thenReturn (hotSeasonSetting);

        BaseSetting baseSetting =new BaseSettingImpl(service, anyString());

        Calendar beforeHotSeason= Calendar.getInstance();
        beforeHotSeason.set(2016,5,19);
        boolean  result1=  baseSetting.isHotSeason(beforeHotSeason.getTime());
        boolean  result2=  baseSetting.isHotSeason(start.getTime());
        Calendar inHotSeason= Calendar.getInstance();
        inHotSeason.set(2016,5,25);
        boolean  result3=  baseSetting.isHotSeason(inHotSeason.getTime());
        boolean  result4=  baseSetting.isHotSeason(end.getTime());
        Calendar afterHotSeason= Calendar.getInstance();
        afterHotSeason.set(2016,5,31);
        boolean  result5=  baseSetting.isHotSeason(afterHotSeason.getTime());

        assertEquals(false,result1);
        assertEquals(true,result2);
        assertEquals(true,result3);
        assertEquals(true,result4);
        assertEquals(false,result5);

    }

    @Test
    public  void hot_season_hotel_fee()
    {
        CharteredPriceSettingService service = mock(CharteredPriceSettingService.class);
        Calendar start= Calendar.getInstance();
        start.set(2016,5,20);

        Calendar end= Calendar.getInstance();
        end.set(2016,5,30);

        HotSeason hotSeasonSetting = new HotSeason(start.getTime(),end.getTime(),30,500);
        when(service.getHotSeason(anyString())).thenReturn (hotSeasonSetting);

        BaseSetting baseSetting =new BaseSettingImpl(service, anyString());

        int  result1=  baseSetting.hotSeasonHotelFee("A", "B");
        assertEquals(500,result1);
    }

    @Test
    public  void hot_season_service_rate()
    {
        CharteredPriceSettingService service = mock(CharteredPriceSettingService.class);
        Calendar start= Calendar.getInstance();
        start.set(2016,5,20);

        Calendar end= Calendar.getInstance();
        end.set(2016,5,30);

        HotSeason hotSeasonSetting = new HotSeason(start.getTime(),end.getTime(),30,500);
        when(service.getHotSeason(anyString())).thenReturn (hotSeasonSetting);

        BaseSetting baseSetting =new BaseSettingImpl(service, anyString());

        int  result1=  baseSetting.hotSeasonServiceRate();
        assertEquals(30,result1);
    }
}
