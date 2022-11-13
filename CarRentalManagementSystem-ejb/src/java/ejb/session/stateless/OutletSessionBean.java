/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.time.LocalTime;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wjahoward
 */
@Stateless
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public OutletSessionBean() {

    }

    @Override
    public Long createNewOutlet(Outlet newOutlet) // throws EmployeeUsernameExistException, UnknownPersistenceException
    {
        em.persist(newOutlet);
        em.flush();

        return newOutlet.getOutletId();
    }

    @Override
    public Outlet getOutlet(String outletName) {
        try {
            Query query = em.createQuery("SELECT o FROM Outlet o WHERE o.address = :inOutletAddress");
            query.setParameter("inOutletAddress", outletName);
            return (Outlet) query.getSingleResult();
        } catch (NoResultException e) {
            
            return null;
        }
    }

    @Override
    public List<Outlet> getOutletWithPickAndReturnTime(LocalTime pickupTime, LocalTime returnTime, String returnOutlet) {
        Query query = em.createQuery("SELECT o FROM Outlet o WHERE o.address = :inReturnOutlet AND ((o.openingTime IS NULL AND o.closingTime IS NULL) OR ((:inReturnTime >= o.openingTime AND :inReturnTime < o.closingTime) AND (:inPickupTime >= o.openingTime AND :inPickupTime <= o.closingTime)))");
        query.setParameter("inReturnOutlet", returnOutlet);
        query.setParameter("inReturnTime", returnTime);
        query.setParameter("inPickupTime", pickupTime);
        return query.getResultList();
    }

    @Override
    public List<Outlet> retrieveAllOutlet() {

        Query query = em.createQuery("SELECT o FROM Outlet o");
        return query.getResultList();
    }

    @Override
    public Outlet getOutletForPickup(LocalTime pickupTime, String outletName) {
        try {
            Query query = em.createQuery("SELECT o FROM Outlet o WHERE o.address = :outlet AND ((o.openingTime IS NULL AND o.closingTime IS NULL) OR (:pickupTime >= o.openingTime AND :pickupTime <= o.closingTime))");
            query.setParameter("outlet", outletName);
            query.setParameter("pickupTime", pickupTime);
            return (Outlet) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Outlet getOutletForReturn(LocalTime returnTime, String outletName) {
        try {
            Query query = em.createQuery("SELECT o FROM Outlet o WHERE o.address = :outlet AND ((o.openingTime IS NULL AND o.closingTime IS NULL) OR (:returnTime >= o.openingTime AND :returnTime <= o.closingTime))");
            query.setParameter("outlet", outletName);
            query.setParameter("returnTime", returnTime);

            return (Outlet) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Outlet> getOutletsAvailableForReturn(LocalTime returnTime) {
        Query query = em.createQuery("SELECT o FROM Outlet o WHERE ((o.openingTime IS NULL AND o.closingTime IS NULL) OR (:returnTime >= o.openingTime AND :returnTime <= o.closingTime))");
        query.setParameter("returnTime", returnTime);
        return query.getResultList();
    }

}
