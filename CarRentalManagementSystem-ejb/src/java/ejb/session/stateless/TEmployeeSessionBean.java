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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.DispatchStatusEnum;

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

}
