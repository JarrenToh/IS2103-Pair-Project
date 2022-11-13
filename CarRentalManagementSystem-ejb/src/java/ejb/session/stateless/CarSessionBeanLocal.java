/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Customer;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wjahoward
 */
@Local
public interface CarSessionBeanLocal {

    Long createCar(Car c, long modelId, long outletId);

    List<Car> getCarsWithCategoryAndModel();

    List<Car> getCars();

    Car getSpecificCar(long carId);
    
    Car getFirstAvailableCarBasedOnMakeAndModel(Car updatedCar) throws CarNotFoundException, UnknownPersistenceException, InputDataValidationException;
    
    List<Car> getAvailableCars(LocalDateTime pickupDateTime, List<Long> carsReservedIds);
    
    List<Car> getUnavailableCars(LocalDateTime pickupDateTime, String pickupOutlet);

    Long UpdateCar(Car updatedCar);

    void updateCarOutlet(Car updatedCar, long outletId);

    void deleteCar(long carId);

    List<Car> retrieveReservedCar();

    boolean carInUse(long carId);
    
    long getNumOfCarsBasedOnMakeAndModel(String make, String model);
    
    Long reserveCar(Car updatedCar, LocalDateTime pickupDateTime, LocalDateTime returnDateTime);
        
}
