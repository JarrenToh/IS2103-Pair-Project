/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.OutletSessionBeanLocal;
import entity.Outlet;
import java.time.LocalTime;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jarrentoh
 */
@Singleton
@LocalBean
@Startup
public class DataInitialisation {

    @EJB
    private OutletSessionBeanLocal outletSessionBean;
    
    
    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    
    public DataInitialisation() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @PostConstruct
    public void postConstruct() {
        if (em.find(Outlet.class, 1L) == null) {
            initializeData();   
        }
    }
    
    private void initializeData()
    {
        LocalTime startTimeOutletA = LocalTime.parse("09:00:00");
        LocalTime endTimeOutletA = LocalTime.parse("18:00:00");
        
        Outlet o1 = new Outlet("A", startTimeOutletA, endTimeOutletA);
        outletSessionBean.createNewOutlet(o1);
    }

    
    
}
