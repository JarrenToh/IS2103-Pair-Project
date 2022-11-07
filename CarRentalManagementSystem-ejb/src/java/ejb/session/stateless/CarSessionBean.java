/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
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
    
    
    
    
    

}
