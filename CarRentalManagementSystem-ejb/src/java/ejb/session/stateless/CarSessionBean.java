/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import entity.Outlet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    public Long createCar(Car c, long modelId) {

        em.persist(c);

        Model associatedModel = em.find(Model.class, modelId);

        associatedModel.getCars().add(c);

        c.setModel(associatedModel);

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
