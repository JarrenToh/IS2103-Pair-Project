/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author wjahoward
 */
@Local
public interface TCustomerSessionBeanLocal {

    Long createCustomer(Customer newCustomer);

    Customer retrieveCustomer(long customerId);
    
    Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;
    
    Customer customerLogin(String username, String password) throws InvalidLoginCredentialException;


}
