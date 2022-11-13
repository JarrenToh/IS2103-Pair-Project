/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Customer;
import entity.Outlet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 *
 * @author wjahoward
 */
@Stateless
public class TCustomerSessionBean implements TCustomerSessionBeanRemote, TCustomerSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createCustomer(Customer newCustomer) {

        em.persist(newCustomer);

        em.flush();
        return newCustomer.getCustomerId();
    }

    @Override
    public Customer retrieveCustomer(long customerId) {

        return em.find(Customer.class, customerId);

    }

}
