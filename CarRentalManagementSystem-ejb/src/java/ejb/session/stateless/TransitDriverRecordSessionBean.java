/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverRecord;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wjahoward
 */
@Stateless
public class TransitDriverRecordSessionBean implements TransitDriverRecordSessionBeanRemote, TransitDriverRecordSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public TransitDriverRecordSessionBean() {
        
    }
    
    @Override
    public void createNewTransitDriverRecord(TransitDriverRecord newTransitDriverRecord) // throws EmployeeUsernameExistException, UnknownPersistenceException
    {
        em.persist(newTransitDriverRecord);
        em.flush();
    }
}
