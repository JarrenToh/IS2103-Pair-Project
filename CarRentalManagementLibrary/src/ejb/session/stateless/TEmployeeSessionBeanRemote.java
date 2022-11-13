/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author wjahoward
 */
@Remote
public interface TEmployeeSessionBeanRemote {
    void createNewEmployee(Employee newEmployee, Long outletId);

    Employee retrieveAvailableEmployeeByOutlet(long OutletId);

    Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;
}
