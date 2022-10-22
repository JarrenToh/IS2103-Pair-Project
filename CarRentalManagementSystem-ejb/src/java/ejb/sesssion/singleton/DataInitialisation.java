/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sesssion.singleton;

import javax.annotation.PostConstruct;
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

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public DataInitialisation() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @PostConstruct
    public void postConstruct() {
    }

    
    
}
