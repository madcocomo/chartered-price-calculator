package com.cartisan.charteredPriceCalculator;

import com.cartisan.charteredPriceCalculator.dto.DayTripResult;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 * Created by colin on 2016/12/17.
 */
public class DayTripTest {
    @Test
    public void one_scope_trip_end_and_driver_in_same_end_scope_calculate_car_fee_and_driver_service_fee(){
        // Arrange
        BaseSetting baseSetting = mockBaseSetting();
        DayTrip dayTrip = generateDayTrip("A:A-A:true:true", baseSetting);

        // Act
        DayTripResult result = dayTrip.calculate();

        // Assert
        assertEquals(730, result.getTotal());
        verify(baseSetting, never()).hotelFee("A", "A");
    }

    @Test
    public void one_scope_trip_not_end_and_driver_in_same_end_scope_calculate_car_fee_and_driver_service_fee(){
        // Arrange
        BaseSetting baseSetting = mockBaseSetting();
        DayTrip dayTrip = generateDayTrip("A:A-A:true:false", baseSetting);

        // Act
        DayTripResult result = dayTrip.calculate();

        // Assert
        assertEquals(730, result.getTotal());
    }

    @Test
    public void one_scope_trip_end_and_driver_not_same_trip_scope_calculate_car_fee_and_driver_service_fee_and_double_driver_alone_fee(){
        // Arrange
        BaseSetting baseSetting = mockBaseSetting();
        DayTrip dayTrip = generateDayTrip("B:A-A:true:true", baseSetting);

        // Act
        DayTripResult result = dayTrip.calculate();

        // Assert
        assertEquals(930, result.getTotal());
        verify(baseSetting, never()).hotelFee("B", "A");
    }

    @Test
    public void one_scope_trip_not_start_not_end_and_driver_not_same_end_scope_calculate_car_fee_and_driver_service_fee_and_hotel_fee(){
        // Arrange
        BaseSetting baseSetting = mockBaseSetting();
        DayTrip dayTrip = generateDayTrip("B:A-A:false:false", baseSetting);

        // Act
        DayTripResult result = dayTrip.calculate();

        // Assert
        assertEquals(930, result.getTotal());
        verify(baseSetting, never()).driverAloneFee("经济6座", "B", "A");
    }

    @Test
    public void one_scope_trip_start_not_end_and_driver_not_same_end_scope_calculate_car_fee_and_driver_service_fee_and_hotel_fee(){
        // Arrange
        BaseSetting baseSetting = mockBaseSetting();
        DayTrip dayTrip = generateDayTrip("B:A-A:true:false", baseSetting);

        // Act
        DayTripResult result = dayTrip.calculate();

        // Assert
        assertEquals(1030, result.getTotal());
    }

    @Test
    public void two_scope_trip_end_calculate_car_fee_and_driver_service_fee_and_driver_alone_fee(){
        // Arrange
        BaseSetting baseSetting = mockBaseSetting();
        DayTrip dayTrip = generateDayTrip("A:A-B:true:true", baseSetting);

        // Act
        DayTripResult result = dayTrip.calculate();

        // Assert
        assertEquals(830, result.getTotal());
        verify(baseSetting, never()).hotelFee("A", "B");

    }

    @Test
    public void two_scope_trip_start_calculate_car_fee_and_driver_service_fee_and_driver_alone_fee(){
        // Arrange
        BaseSetting baseSetting = mockBaseSetting();
        DayTrip dayTrip = generateDayTrip("B:A-B:true:true", baseSetting);

        // Act
        DayTripResult result = dayTrip.calculate();

        // Assert
        assertEquals(830, result.getTotal());
    }

    @Test
    public void two_scope_trip_not_end_calculate_car_fee_and_driver_service_fee_and_hotel_fee(){
        // Arrange
        BaseSetting baseSetting = mockBaseSetting();
        DayTrip dayTrip = generateDayTrip("A:A-B:true:false", baseSetting);

        // Act
        DayTripResult result = dayTrip.calculate();

        // Assert
        assertEquals(930, result.getTotal());
        verify(baseSetting, never()).driverAloneFee("经济6座", "A", "B");
    }

    @Test
    public void multiple_scope_trip_end_calculate_car_fee_and_driver_service_fee(){
        // Arrange
        BaseSetting baseSetting = mockBaseSetting();
        DayTrip dayTrip = generateDayTrip("A:A-B-C-D:true:true", baseSetting);

        // Act
        DayTripResult result = dayTrip.calculate();

        // Assert
        assertEquals(1330, result.getTotal());
        verify(baseSetting, never()).hotelFee("D", "D");
    }

    @Test
    public void discount_calculate(){
        // Arrange
        BaseSetting baseSetting = mockBaseSetting();
        DayTrip dayTrip = generateDayTrip("A:A-A:false:false", baseSetting, 97);

        // Act
        DayTripResult result = dayTrip.calculate();

        // Assert
        assertEquals(715, result.getTotal());
    }

    @Test
    public void hot_season_calculate_use_hot_service_rate_and_hot_hotel_fee(){
        // Arrange
        Calendar date= Calendar.getInstance();
        date.set(2016,5,30);

        BaseSetting baseSetting = mockHotSeasonBaseSetting(date.getTime());
        DayTrip dayTrip = generateDayTrip("B:A-A:false:false", date.getTime(), baseSetting, 100);

        // Act
        DayTripResult result = dayTrip.calculate();

        // Assert
        assertEquals(1349, result.getTotal());
    }

    @Test
    public void hot_season_calculate_use_hot_service_rate(){
        // Arrange
        Calendar date= Calendar.getInstance();
        date.set(2016,5,30);

        BaseSetting baseSetting = mockHotSeasonBaseSetting(date.getTime());
        DayTrip dayTrip = generateDayTrip("A:A-A:false:false", date.getTime(), baseSetting, 100);

        // Act
        DayTripResult result = dayTrip.calculate();

        // Assert
        assertEquals(949, result.getTotal());
    }

    private BaseSetting mockHotSeasonBaseSetting(Date tripDate){
        BaseSetting baseSetting = mockBaseSetting();

        when(baseSetting.hotSeasonServiceRate()).thenReturn(30);
        when(baseSetting.hotSeasonHotelFee("B", "A")).thenReturn(400);

        when(baseSetting.isHotSeason(tripDate)).thenReturn(true);

        return baseSetting;
    }

    private BaseSetting mockBaseSetting() {
        BaseSetting baseSetting = mock(BaseSetting.class);

        when(baseSetting.carFee("经济6座", "A", "A")).thenReturn(230);
        when(baseSetting.carFee("经济6座", "A", "B")).thenReturn(230);
        when(baseSetting.carFee("经济6座", "B", "C")).thenReturn(220);
        when(baseSetting.carFee("经济6座", "C", "D")).thenReturn(180);


        when(baseSetting.driverServiceFee()).thenReturn(500);

        when(baseSetting.hotelFee("B", "A")).thenReturn(200);
        when(baseSetting.hotelFee("A", "B")).thenReturn(200);


        when(baseSetting.driverAloneFee("经济6座", "B", "A")).thenReturn(100);
        when(baseSetting.driverAloneFee("经济6座", "A", "B")).thenReturn(100);
        when(baseSetting.driverAloneFee("经济6座", "A", "D")).thenReturn(200);

        when(baseSetting.discount(3)).thenReturn(97);

        return baseSetting;
    }

    private DayTrip generateDayTrip(String expression, BaseSetting baseSetting){
        return generateDayTrip(expression, new Date(), baseSetting, 100);
    }

    private DayTrip generateDayTrip(String expression, BaseSetting baseSetting, int discount){
        return generateDayTrip(expression, new Date(), baseSetting, discount);
    }

    private DayTrip generateDayTrip(String expression, Date date, BaseSetting baseSetting, int discount){
        String[] settings = expression.split(":");
        List<String> path = asList(settings[1].split("-"));
        return new DayTrip("", date, "经济6座", settings[0], discount, path,
                Boolean.parseBoolean(settings[2]), Boolean.parseBoolean(settings[3]), baseSetting );
    }
}
