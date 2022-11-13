/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

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

    @Override
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException {

        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.userName = :username");
        query.setParameter("username", username);

        try {
            return (Customer) query.getSingleResult();

        } catch (NoResultException | NonUniqueResultException ex) {

            throw new CustomerNotFoundException("Customer Username " + username + " does not exist!");
        }
    }

    @Override
    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException {

        try {
            Customer customer = retrieveCustomerByUsername(username);

            if (customer.getPassword().equals(password)) {
                return customer;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (CustomerNotFoundException customerNotFoundException) {

            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

}
