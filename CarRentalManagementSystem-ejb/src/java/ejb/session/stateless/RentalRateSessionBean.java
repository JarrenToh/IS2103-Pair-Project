/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.RentalRate;
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
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public long createRentalRate(RentalRate rr, Category c) {        
        em.persist(rr);
        
        rr.setCategory(c);
        
        em.flush();
        
        return rr.getId();
    }
    
    @Override
    public List<RentalRate> getRentalRatesWithCategories() {
        Query query = em.createQuery("SELECT r FROM RentalRate r INNER JOIN r.category c ORDER BY c.categoryName, r.startDateTime, r.endDateTime ASC");
        return query.getResultList();
    }
    
    @Override
    public List<RentalRate> getRentalRates() {
        Query query = em.createQuery("SELECT r FROM RentalRate r");
        return query.getResultList();
    }
    
    @Override
    public RentalRate getSpecificRental(long rentalRateId) {
        Query query = em.createQuery("SELECT r FROM RentalRate r WHERE r.id = :inRentalRateId");
        query.setParameter("inRentalRateId", rentalRateId);
        return (RentalRate)query.getSingleResult();
    }
    
    @Override
    public long updateRentalRate(RentalRate updatedRentalRate) {        
        RentalRate rentalRateToUpdate = getSpecificRental(updatedRentalRate.getId());
        rentalRateToUpdate.setCategory(updatedRentalRate.getCategory());
        rentalRateToUpdate.setName(updatedRentalRate.getName());
        rentalRateToUpdate.setRatePerDay(updatedRentalRate.getRatePerDay());
        rentalRateToUpdate.setStartDateTime(updatedRentalRate.getStartDateTime());
        rentalRateToUpdate.setEndDateTime(updatedRentalRate.getEndDateTime());
        
        return updatedRentalRate.getId();
    }
    
    @Override
    public void deleteRentalRate(long rentalRateId) {
        RentalRate rentalRateToRemove = getSpecificRental(rentalRateId);
        em.remove(rentalRateToRemove);
    }
    
}
