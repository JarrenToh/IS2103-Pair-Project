/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Customer;
import entity.Model;
import entity.Outlet;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CarStatusEnum;
import util.enumeration.LocationEnum;

/**
 *
 * @author wjahoward
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public CarSessionBean() {
    }

    @Override
    public Long createCar(Car c, long modelId, long outletId) {

        em.persist(c);

        Model associatedModel = em.find(Model.class, modelId);
        Outlet associatedOutlet = em.find(Outlet.class, outletId);

        associatedModel.getCars().add(c);
        associatedOutlet.getCars().add(c);

        c.setModel(associatedModel);
        c.setOutlet(associatedOutlet);

        em.flush();

        return c.getCarId();

    }

    @Override
    public List<Car> getCars() {
        Query query = em.createQuery("SELECT c FROM Car c");
        return query.getResultList();
    }

    @Override
    public List<Car> getCarsWithCategoryAndModel() {

        Query query = em.createQuery("SELECT c FROM Car c INNER JOIN c.model m INNER JOIN m.category cat ORDER BY cat.categoryName, m.make, m.model, c.licensePlateNumber ASC");
        return query.getResultList();
    }

    @Override
    public Car getSpecificCar(long carId) {

        return em.find(Car.class, carId);

    }

    @Override
    public List<Car> getAvailableCars(LocalDateTime pickupDateTime) {
        List<Car> availableCars = new ArrayList<>();

        Query query = em.createQuery("SELECT c FROM Car c WHERE c.status = :inStatus AND ((c.outlet.openingTime IS NULL AND c.outlet.closingTime IS NULL) OR (:pickupTime >= c.outlet.openingTime AND :pickupTime <= c.outlet.closingTime))");
        query.setParameter("inStatus", CarStatusEnum.AVAILABLE);
        query.setParameter("pickupTime", pickupDateTime.toLocalTime());
        availableCars = query.getResultList();
//
//        // get unavailable cars that are either rented out or haven't rented out
//        Query query2 = em.createQuery("SELECT c FROM Car c WHERE c.status = :inStatus");
//        query2.setParameter("inOutletId", outletId);
//        query2.setParameter("inStatus", CarStatusEnum.UNAVAILABLE);
//        List<Car> unavailableCars = query.getResultList();
//        unavailableCars = unavailableCars.stream()
//                                        .filter(c -> c.getRentalEndDate() != null && Math.abs(ChronoUnit.HOURS.between(c.getRentalEndDate(), pickupDateTime)) >= 2)
//                                        .collect(Collectors.toList());
//
//        cars.addAll(availableCars);
//        cars.addAll(unavailableCars);
        return availableCars;
    }
    
    @Override
    public List<Car> getUnavailableCars(LocalDateTime pickupDateTime, String pickupOutlet) {
        List<Car> unavailableCars = new ArrayList<>();
        
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.status = :inStatus AND ((c.outlet.openingTime IS NULL AND c.outlet.closingTime IS NULL) OR (:pickupTime >= c.outlet.openingTime AND :pickupTime <= c.outlet.closingTime))");
        query.setParameter("inStatus", CarStatusEnum.UNAVAILABLE);
        query.setParameter("pickupTime", pickupDateTime.toLocalTime());
        unavailableCars = query.getResultList();
        
        unavailableCars = unavailableCars.stream()
                                        .filter(c -> c.getOutlet().getAddress().equals(pickupOutlet) && pickupDateTime.isBefore(c.getRentalEndDate())) // same outlet
                                        .filter(c -> !c.getOutlet().getAddress().equals(pickupOutlet) && Math.abs(ChronoUnit.HOURS.between(c.getRentalEndDate(), pickupDateTime)) >= 2) // differemt outlet
                                        .collect(Collectors.toList());
        
        return unavailableCars;
    }

    //Excluded association updates
    @Override
    public Long UpdateCar(Car updatedCar) {

        Car carToUpdate = getSpecificCar(updatedCar.getCarId());

        carToUpdate.setLicensePlateNumber(updatedCar.getLicensePlateNumber());
        carToUpdate.setColour(updatedCar.getColour());
        carToUpdate.setStatus(updatedCar.getStatus());
        carToUpdate.setLocation(updatedCar.getLocation());
        carToUpdate.setEnabled(updatedCar.isEnabled());
        carToUpdate.setRentalStartDate(updatedCar.getRentalStartDate());
        carToUpdate.setRentalEndDate(updatedCar.getRentalEndDate());
        return updatedCar.getCarId();
    }

    @Override
    public void updateCarOutlet(Car car, long outletId) {

        Outlet newOutlet = em.find(Outlet.class, outletId);
        Outlet currentOutlet = em.find(Outlet.class, car.getOutlet().getOutletId());
        Car currentCar = em.find(Car.class, car.getCarId());

        currentOutlet.getCars().remove(currentCar);
        newOutlet.getCars().add(currentCar);
        currentCar.setOutlet(newOutlet);

    }

    @Override
    public void deleteCar(long carId) {

        Car carToRemove = getSpecificCar(carId);
        em.remove(carToRemove);

    }

    @Override
    public List<Car> retrieveReservedCar() {

        Query query = em.createQuery("SELECT c FROM Car c INNER JOIN c.model m INNER JOIN m.category cat INNER JOIN cat.rentalRates r WHERE c.status = :carStatus");
        query.setParameter("carStatus", CarStatusEnum.UNAVAILABLE);
        return query.getResultList();
    }

    @Override
    public long getNumOfCarsBasedOnMakeAndModel(String make, String model) {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.model.make = :make AND c.model.model = :model");
        query.setParameter("make", make);
        query.setParameter("model", model);
        return query.getResultList().size();
    }

    @Override
    public boolean carInUse(long carId) {

        Query query = em.createQuery("SELECT c FROM Car c WHERE c.status = :carStatus and c.id = :carId");
        query.setParameter("carStatus", CarStatusEnum.UNAVAILABLE);
        query.setParameter("carId", carId);
        return !query.getResultList().isEmpty();

    }

}
