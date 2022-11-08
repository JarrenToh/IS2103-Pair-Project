/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Employee;
import entity.TransitDriverRecord;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CarStatusEnum;
import util.enumeration.DispatchStatusEnum;

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
    public void createNewTransitDriverRecord(TransitDriverRecord newTransitDriverRecord, long CarId) // throws EmployeeUsernameExistException, UnknownPersistenceException
    {
        em.persist(newTransitDriverRecord);
        Car associatedCar = em.find(Car.class, CarId);
        
        //Association
        newTransitDriverRecord.setCar(associatedCar);
        associatedCar.setTransitDriverRecord(newTransitDriverRecord);
        
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
        
        //If employee DispatchStatus is null/completed, we can assign employee?
        currentEmployee.setDispatchStatus(DispatchStatusEnum.INCOMPLETE);
        

        Car carToUpdate = em.find(Car.class, currentTransitDriverRecord.getCar().getCarId());
        carToUpdate.setStatus(CarStatusEnum.TRANSIT);
        
        //association
        currentTransitDriverRecord.setEmployee(currentEmployee);
        currentEmployee.setTransitDriverRecord(currentTransitDriverRecord);
        em.flush();

    }

    @Override
    public void updateTransitDriverRecordAsCompleted(long transitDriverId) {
        
        //Do we need to disassociate the employee?
        TransitDriverRecord currentTransitDriverRecord = getSpecificTransitDriverRecord(transitDriverId);
        
        Car carToUpdate = em.find(Car.class, currentTransitDriverRecord.getCar().getCarId());
        carToUpdate.setStatus(CarStatusEnum.AVAILABLE);
        
        Employee employeeToUpdate = em.find(Employee.class, currentTransitDriverRecord.getEmployee().getEmployeeId());
        employeeToUpdate.setDispatchStatus(DispatchStatusEnum.COMPLETED);
        
        currentTransitDriverRecord.setCompleted(true);
        
    }

}
