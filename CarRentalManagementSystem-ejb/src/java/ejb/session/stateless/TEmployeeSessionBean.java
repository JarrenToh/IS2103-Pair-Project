/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.DispatchStatusEnum;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author wjahoward
 */
@Stateless
public class TEmployeeSessionBean implements TEmployeeSessionBeanRemote, TEmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public TEmployeeSessionBean() {

    }

    @Override
    public void createNewEmployee(Employee newEmployee, Long outletId) // throws EmployeeUsernameExistException, UnknownPersistenceException
    {
        Outlet outlet = em.find(Outlet.class, outletId);

        em.persist(newEmployee);

        newEmployee.setOutlet(outlet);
        outlet.getEmployees().add(newEmployee);

        em.flush();
    }

    @Override
    public Employee retrieveAvailableEmployeeByOutlet(long OutletId) {

        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.outlet.outletId = :oId AND e.dispatchStatus <> :status");
        query.setParameter("oId", OutletId);
        query.setParameter("status", DispatchStatusEnum.COMPLETED);
        return (Employee) query.getSingleResult();
    }

        @Override
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException {
        
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.userName = :username");
        query.setParameter("username", username);
        
            try {
                return (Employee) query.getSingleResult();
                
            } catch (NoResultException | NonUniqueResultException ex) {
                
                throw new EmployeeNotFoundException("Employee Username " + username + " does not exist!");
            }
    }
    
    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException {

        

        try {
            Employee employee = retrieveEmployeeByUsername(username);
            
            if (employee.getPassword().equals(password)) {
                employee.getOutlet();
                employee.getTransitDriverRecord();
                return employee;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (EmployeeNotFoundException employeeNotFoundException) {
            
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }



}
