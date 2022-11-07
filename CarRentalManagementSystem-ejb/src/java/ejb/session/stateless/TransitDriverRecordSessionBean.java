/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.TransitDriverRecord;
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

    @Override
    public List<TransitDriverRecord> getTransitDriverRecord() {

        Query query = em.createQuery("SELECT td FROM TransitDriverRecord td");
        return query.getResultList();
    }

    @Override
    public TransitDriverRecord getSpecificTransitDriverRecord(long transitDriverId) {
        
        return em.find(TransitDriverRecord.class, transitDriverId);
    }

    @Override
    public void assignTransitDriver(long transitDriverId, long employeeId) {

        TransitDriverRecord currentTransitDriverRecord = em.find(TransitDriverRecord.class, transitDriverId);
        Employee currentEmployee = em.find(Employee.class, employeeId);

        //association
        currentTransitDriverRecord.setEmployee(currentEmployee);
        currentEmployee.setTransitDriverRecord(currentTransitDriverRecord);
        em.flush();

    }

    @Override
    public void updateTransitDriverRecordAsCompleted(long transitDriverId) {
        
        TransitDriverRecord currentTransitDriverRecord = getSpecificTransitDriverRecord(transitDriverId);
        currentTransitDriverRecord.setCompleted(true);
        
    }

}
