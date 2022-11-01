/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wjahoward
 */
@Stateless
public class CategorySessionBean implements CategorySessionBeanRemote, CategorySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public CategorySessionBean() {
        
    }
    
    @Override
    public void createNewCategory(Category newCategory) // throws EmployeeUsernameExistException, UnknownPersistenceException
    {
        em.persist(newCategory);
        em.flush();
    }
}
