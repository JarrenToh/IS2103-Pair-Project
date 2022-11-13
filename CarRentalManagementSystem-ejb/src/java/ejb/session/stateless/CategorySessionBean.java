/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wjahoward
 */
@Stateless
public class CategorySessionBean implements CategorySessionBeanRemote, CategorySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CategorySessionBean() {

        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public void createNewCategory(Category newCategory) throws EmployeeUsernameExistException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<Category>> constraintViolations = validator.validate(newCategory);

        if (constraintViolations.isEmpty()) {
            try {
                
                em.persist(newCategory);
                em.flush();
                
            } catch(PersistenceException ex) {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new EmployeeUsernameExistException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<Category> getCategories() {
        Query query = em.createQuery("SELECT c FROM Category c");
        return query.getResultList();
    }

    @Override
    public Category getCategory(String categoryName) {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.categoryName = :inCategoryName");
        query.setParameter("inCategoryName", categoryName);
        return (Category) query.getSingleResult();
    }

    // this function is for testing purpose by Howard earlier on, not part of the use cases :)
    @Override
    public void deleteCategory() {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.id = :inCategoryId");
        query.setParameter("inCategoryId", 1L);
        Category cat = (Category) query.getSingleResult();
        em.remove(cat);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Category>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
