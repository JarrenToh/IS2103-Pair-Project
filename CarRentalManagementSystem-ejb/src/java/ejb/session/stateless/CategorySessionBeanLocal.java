/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import java.util.List;
import javax.ejb.Local;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wjahoward
 */
@Local
public interface CategorySessionBeanLocal {
    void createNewCategory(Category newCategory) throws EmployeeUsernameExistException, UnknownPersistenceException, InputDataValidationException;
    List<Category> getCategories();
    void deleteCategory();
    Category getCategory(String categoryName);
}
