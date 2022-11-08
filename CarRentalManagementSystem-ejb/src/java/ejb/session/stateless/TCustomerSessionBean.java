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
import util.enumeration.CarStatusEnum;
import util.enumeration.LocationEnum;

/**
 *
 * @author wjahoward
 */
@Stateless
public class TCustomerSessionBean implements TCustomerSessionBeanRemote, TCustomerSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createCustomer(Customer newCustomer, long OutletId) {

        em.persist(newCustomer);
        Outlet currentOutlet = em.find(Outlet.class, OutletId);

        //Association
        newCustomer.setOutlet(currentOutlet);
        currentOutlet.getCustomers().add(newCustomer);

        em.flush();
        return newCustomer.getCustomerId();
    }

    @Override
    public Customer retrieveCustomer(long customerId) {

        return em.find(Customer.class, customerId);

    }

    @Override
    public void updateCustomerPaymentStatus(Customer customer) {

        Customer customerToUpdate = em.find(Customer.class, customer.getCustomerId());
        customerToUpdate.setPaid(customer.isPaid());

    }

    @Override
    public void pickUpCar(long customerId, long carId) {

        Customer pickUpCustomer = em.find(Customer.class, customerId);
        Car pickUpCar = em.find(Car.class, carId);

        pickUpCustomer.setCar(pickUpCar);
        pickUpCar.setCustomer(pickUpCustomer);

        pickUpCar.setLocation(LocationEnum.SPECIFIC_CUSTOMER);
        pickUpCar.setStatus(CarStatusEnum.UNAVAILABLE);

    }

    @Override
    public void returnCar(long carId) {

        Car returnCar = em.find(Car.class, carId);
        Customer returnCustomer = em.find(Customer.class, returnCar.getCustomer().getCustomerId());

        returnCar.setLocation(LocationEnum.OUTLET);
        returnCar.setStatus(CarStatusEnum.AVAILABLE);
        
        returnCar.setCustomer(null);   
        returnCustomer.setCar(null);
    }

}
