/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.RentalRate;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RentalRateType;

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
        
        Category associatedCategory = em.find(Category.class, c.getId());
        
        rr.setCategory(associatedCategory);
        
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
    public List<RentalRate> getRentalRatesByCategoryId(long categoryId) {
        Query query = em.createQuery("SELECT r FROM RentalRate r where r.category.id = :inCategoryId");
        query.setParameter("inCategoryId", categoryId);
        return query.getResultList();
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
    
    @Override
    public RentalRate getRentalRatePriceByDateTimeAndType(List<Long> rentalRatesIds, LocalDateTime tempDateTime, RentalRateType rentalRateType) {
        Query query = em.createQuery("SELECT r FROM RentalRate r WHERE r.id IN :inRentalRatesIds AND r.rentalRateType = :inRentalRateType AND ((r.startDateTime IS NULL AND r.endDateTime IS NULL) OR (:inTempDateTime >= r.startDateTime AND :inTempDateTime < r.endDateTime)) ORDER BY r.ratePerDay ASC");
        query.setParameter("inRentalRatesIds", rentalRatesIds);
        query.setParameter("inTempDateTime", tempDateTime);
        query.setParameter("inRentalRateType", rentalRateType);
        List<RentalRate> rentalRates = query.getResultList();
        if (!rentalRates.isEmpty()) {
            return rentalRates.get(0);
        }
        
        return null;
    }
    
}
