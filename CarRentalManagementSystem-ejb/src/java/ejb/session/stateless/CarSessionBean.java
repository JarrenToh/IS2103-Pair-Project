/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Category;
import entity.Customer;
import entity.Model;
import entity.Outlet;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarStatusEnum;
import util.enumeration.LocationEnum;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wjahoward
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CarSessionBean() {

        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
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
    public Car getFirstAvailableCarBasedOnMakeAndModel(Car updatedCar) throws CarNotFoundException, UnknownPersistenceException, InputDataValidationException {

        if (updatedCar != null && updatedCar.getCarId() != null) {

            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(updatedCar);

            Query query = null;
            if (constraintViolations.isEmpty()) {
                try {
                    query = em.createQuery("SELECT c FROM Car c WHERE c.status = :status AND c.model.make = :make AND c.model.model = :model");
                    query.setParameter("status", CarStatusEnum.AVAILABLE);
                    query.setParameter("make", updatedCar.getModel().getMake());
                    query.setParameter("model", updatedCar.getModel().getModel());
                    return (Car) query.getResultList().get(0);

                } catch (PersistenceException ex) {
                    if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {

                        if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {

                            throw new CarNotFoundException();

                        } else {
                            throw new UnknownPersistenceException(ex.getMessage());
                        }
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
            } else {

                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }

        } else {

            throw new CarNotFoundException("Car ID Not provided for Car!");
        }
    }

    @Override
    public List<Car> getAvailableCars(LocalDateTime pickupDateTime, List<Long> carsReservedIds) {
        List<Car> availableCars = new ArrayList<>();

        Query query = null;

        if (carsReservedIds.isEmpty()) {
            query = em.createQuery("SELECT c FROM Car c WHERE c.status = :inStatus AND ((c.outlet.openingTime IS NULL AND c.outlet.closingTime IS NULL) OR (:pickupTime >= c.outlet.openingTime AND :pickupTime <= c.outlet.closingTime))");
            query.setParameter("inStatus", CarStatusEnum.AVAILABLE);
            query.setParameter("pickupTime", pickupDateTime.toLocalTime());
        } else {
            query = em.createQuery("SELECT c FROM Car c WHERE c.status = :inStatus AND c.carId NOT IN :carsReservedIds AND ((c.outlet.openingTime IS NULL AND c.outlet.closingTime IS NULL) OR (:pickupTime >= c.outlet.openingTime AND :pickupTime <= c.outlet.closingTime))");
            query.setParameter("inStatus", CarStatusEnum.AVAILABLE);
            query.setParameter("carsReservedIds", carsReservedIds);
            query.setParameter("pickupTime", pickupDateTime.toLocalTime());
        }
        availableCars = query.getResultList();

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
                .filter(c -> c.getOutlet().getAddress().equals(pickupOutlet) && pickupDateTime.isBefore(c.getReserved().getRentalEndDate())) // same outlet
                .filter(c -> !c.getOutlet().getAddress().equals(pickupOutlet) && Math.abs(ChronoUnit.HOURS.between(c.getReserved().getRentalEndDate(), pickupDateTime)) >= 2) // differemt outlet
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
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.status = :status AND c.model.make = :make AND c.model.model = :model");
        query.setParameter("status", CarStatusEnum.AVAILABLE);
        query.setParameter("make", make);
        query.setParameter("model", model);
        return query.getResultList().size();
    }

    @Override
    public boolean carInUse(long carId) {

        Query query = em.createQuery("SELECT c FROM Car c WHERE c.status = :carStatus and c.carId = :carId");
        query.setParameter("carStatus", CarStatusEnum.UNAVAILABLE);
        query.setParameter("carId", carId);
        return !query.getResultList().isEmpty();

    }

    @Override
    public Long reserveCar(Car updatedCar, LocalDateTime pickupDateTime, LocalDateTime returnDateTime) {

//        Car carToUpdate = getFirstAvailableCarBasedOnMakeAndModel(updatedCar);
//
//        carToUpdate.setStatus(CarStatusEnum.UNAVAILABLE);
//        carToUpdate.setRentalStartDate(pickupDateTime);
//        carToUpdate.setRentalEndDate(returnDateTime);
//
//        return updatedCar.getCarId();
        return 1L;

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Car>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public List<Car> getAvailableCarsByOutlet(Model m, String pickupOutlet) {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.status = :inStatus AND (c.model.make = :make AND c.model.model = :model) AND c.outlet.address = :pickupOutlet");
        query.setParameter("inStatus", CarStatusEnum.AVAILABLE);
        query.setParameter("make", m.getMake());
        query.setParameter("model", m.getModel());
        query.setParameter("pickupOutlet", pickupOutlet);
        return query.getResultList();
    }
    
     @Override
    public List<Car> getUnavailableCarsByOutlet(Model m, String pickupOutlet, LocalDateTime pickupDateTime) {
        List<Car> unavailableCars = new ArrayList<>();

        Query query = em.createQuery("SELECT c FROM Car c WHERE c.status = :inStatus AND (c.model.make = :make AND c.model.model = :model) AND c.outlet.address = :pickupOutlet");
        query.setParameter("inStatus", CarStatusEnum.UNAVAILABLE);
        query.setParameter("make", m.getMake());
        query.setParameter("model", m.getModel());
        query.setParameter("pickupOutlet", pickupOutlet);
        unavailableCars = query.getResultList();

        unavailableCars = unavailableCars.stream()
                .filter(c -> c.getOutlet().getAddress().equals(pickupOutlet) && pickupDateTime.isBefore(c.getReserved().getRentalEndDate())) // same outlet
                .filter(c -> !c.getOutlet().getAddress().equals(pickupOutlet) && Math.abs(ChronoUnit.HOURS.between(c.getReserved().getRentalEndDate(), pickupDateTime)) >= 2) // differemt outlet
                .collect(Collectors.toList());

        return unavailableCars;
    }

}
