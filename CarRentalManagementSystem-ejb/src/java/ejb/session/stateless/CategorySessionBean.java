/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    
    @Override
    public List<Category> getCategories() {        
        Query query = em.createQuery("SELECT c FROM Category c");
        return query.getResultList();
    }
    
    @Override
    public void deleteCategory() {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.id = :inCategoryId");
        query.setParameter("inCategoryId", 1L);
        Category cat = (Category)query.getSingleResult();
//        c = em.merge(c);
        em.remove(cat);
    }
}
