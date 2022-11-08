/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;

/**
 *
 * @author wjahoward
 */
@Local
public interface TCustomerSessionBeanLocal {

    Long createCustomer(Customer newCustomer, long OutletId);

    Customer retrieveCustomer(long customerId);

    void updateCustomerPaymentStatus(Customer customer);

    void pickUpCar(long customerId, long carId);

    void returnCar(long carId);


}
