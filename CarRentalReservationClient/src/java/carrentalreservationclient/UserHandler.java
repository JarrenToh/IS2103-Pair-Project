/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservedSessionBeanRemote;
import entity.Car;
import entity.Category;
import entity.Model;
import entity.Outlet;
import entity.RentalRate;
import entity.Reserved;
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

    public static List<Car> getAvailableCars(CarSessionBeanRemote carSessionBeanRemote, List<Long> carsReservedIds, LocalDateTime pickupDateTime) {
        return carSessionBeanRemote.getAvailableCars(pickupDateTime, carsReservedIds);
    }

    public static List<Car> getUnavailableCars(CarSessionBeanRemote carSessionBeanRemote, LocalDateTime pickupDateTime, String pickupOutlet) {
        return carSessionBeanRemote.getUnavailableCars(pickupDateTime, pickupOutlet);
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

    public static Outlet getOutletForPickup(OutletSessionBeanRemote outletSessionBeanRemote, LocalDateTime pickupDateTime, String pickupOutlet) {
        Outlet outlet = outletSessionBeanRemote.getOutletForPickup(pickupDateTime.toLocalTime(), pickupOutlet);
        return outlet;
    }

    public static boolean checkReturnAvailability(OutletSessionBeanRemote outletSessionBeanRemote, LocalDateTime returnDateTime, String returnOutlet) {
        Outlet outlet = outletSessionBeanRemote.getOutletForReturn(returnDateTime.toLocalTime(), returnOutlet);
        return outlet != null;
    }

    public static List<Outlet> getOutletsAvailableForReturn(OutletSessionBeanRemote outletSessionBeanRemote, LocalDateTime returnDateTime) {
        List<Outlet> outlets = outletSessionBeanRemote.getOutletsAvailableForReturn(returnDateTime.toLocalTime());
        return outlets;
    }

    public static void reserveCar(CarSessionBeanRemote carSessionBeanRemote, ReservedSessionBeanRemote reservedSessionBeanRemote, Car car, long customerId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, Reserved reserved) {
        carSessionBeanRemote.reserveCar(car, pickupDateTime, returnDateTime);
//        reservedSessionBeanRemote.createNewReservation(reserved, car.getCarId(), customerId);
    }

    public static Model getModelByMakeAndModel(ModelSessionBeanRemote modelSessionBeanRemote, String make, String model) {
        return modelSessionBeanRemote.getModelByMakeAndModel(make, model);
    }

    public static Category getCategoryByCategoryName(CategorySessionBeanRemote categorySessionBeanRemote, String category) {
        return categorySessionBeanRemote.getCategory(category);
    }

    public static List<Reserved> getReservedRecords(ReservedSessionBeanRemote reservedSessionBeanRemote) {
        return reservedSessionBeanRemote.getReservedRecords();
    }

    public static List<Reserved> getNumberOfMReservedByOutlet(ReservedSessionBeanRemote reservedSessionBeanRemote, Model m, String pickupOutlet) {
        return reservedSessionBeanRemote.getNumberOfMReservedByOutlet(m, pickupOutlet);
    }
    
    public static List<Car> getAvailableCarsByOutlet(CarSessionBeanRemote carSessionBeanRemote, Model m, String pickupOutlet) {
        return carSessionBeanRemote.getAvailableCarsByOutlet(m, pickupOutlet);
    }
    
    public static List<Car> getUnavailableCarsByOutlet(CarSessionBeanRemote carSessionBeanRemote, Model m, String pickupOutlet, LocalDateTime pickupDateTime) {
        return carSessionBeanRemote.getUnavailableCarsByOutlet(m, pickupOutlet, pickupDateTime);
    }
    
    public static void reserve(ReservedSessionBeanRemote reservedSessionBeanRemote, long customerId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, Reserved reserved) {
        reservedSessionBeanRemote.createReservation(reserved, customerId);
    }
}
