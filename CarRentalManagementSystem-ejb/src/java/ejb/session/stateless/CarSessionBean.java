/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
    


    
    
}
