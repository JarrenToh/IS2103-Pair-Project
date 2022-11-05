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
        em.flush();
        
        return rr.getId();
    }
    
    @Override
    public List<RentalRate> getRentalRates() {
        Query query = em.createQuery("SELECT r FROM RentalRate r INNER JOIN r.category c ORDER BY c.categoryName, r.validityPeriod ASC");
        return query.getResultList();
    }
    
}
