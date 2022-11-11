/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Car;
import entity.Outlet;
import entity.RentalRate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import util.enumeration.RentalRateType;

/**
 *
 * @author wjahoward
 */
public class UserHandler {
    
    public static Outlet getOutletByName(OutletSessionBeanRemote outletSessionBeanRemote, String outletName) {
        return outletSessionBeanRemote.getOutlet(outletName);
    }

    public static List<Outlet> getOutletsForPickAndReturn(OutletSessionBeanRemote outletSessionBeanRemote, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, String returnOutlet) {
        return outletSessionBeanRemote.getOutletWithPickAndReturnTime(pickupDateTime.toLocalTime(), returnDateTime.toLocalTime(), returnOutlet);
    }

    public static List<Car> getCarsByOutletId(CarSessionBeanRemote carSessionBeanRemote, long outletId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime) {
        return carSessionBeanRemote.getCarsByOutletId(outletId, pickupDateTime, returnDateTime);
    }

    public static List<RentalRate> getRentalRatesByCategoryId(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, long categoryId) {
        return rentalRateSessionBeanRemote.getRentalRatesByCategoryId(categoryId);
    }
    
    public static long getNumOfCarsBasedOnMakeAndModel(CarSessionBeanRemote carSessionBeanRemote, String make, String model) {
        return carSessionBeanRemote.getNumOfCarsBasedOnMakeAndModel(make, model);
    }

    public static BigDecimal getRentalRatePriceByDateTimeAndType(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, List<Long> rentalRatesId, LocalDateTime tempDateTime, RentalRateType rentalRateType) {
        RentalRate rr = rentalRateSessionBeanRemote.getRentalRatePriceByDateTimeAndType(rentalRatesId, tempDateTime, rentalRateType);
        if (rr != null) {
            return rr.getRatePerDay();
        }

        return null;
    }

}
