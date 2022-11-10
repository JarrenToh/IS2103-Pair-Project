/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
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

        Query query = em.createQuery("SELECT c FROM Car c INNER JOIN c.model m INNER JOIN m.category cat ORDER BY cat.categoryName, m.makeAndModelName, c.licensePlateNumber ASC");
        return query.getResultList();
    }

    @Override
    public Car getSpecificCar(long carId) {
        
        return em.find(Car.class, carId);
        
    }
    
    @Override
    public List<Car> getCarsByOutletId(long outletId, LocalDateTime pickupDateTime) {
        List<CarStatusEnum> carStatuses = new ArrayList<>();
        carStatuses.add(CarStatusEnum.AVAILABLE);
        carStatuses.add(CarStatusEnum.UNAVAILABLE);
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.outlet.outletId = :inOutletId AND c.status IN :inStatuses");
        query.setParameter("inOutletId", outletId);
        query.setParameter("inStatuses", carStatuses);
        List<Car> cars = query.getResultList();
        
        cars = cars.stream().filter(c -> {         
            return c.getRentalEndDate() == null || (c.getRentalEndDate() != null && Math.abs(ChronoUnit.HOURS.between(c.getRentalEndDate(), pickupDateTime)) >= 2);
                }).collect(Collectors.toList());
        
        return cars;
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
    
    


}
