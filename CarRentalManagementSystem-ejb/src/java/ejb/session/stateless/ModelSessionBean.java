/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.Model;
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
public class ModelSessionBean implements ModelSessionBeanRemote, ModelSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public long createModel(Model m, Category c) {
        em.persist(m);

        m.setCategory(c);

        em.flush();

        return m.getId();
    }

    @Override
    public List<Model> getModels() {
        Query query = em.createQuery("SELECT m FROM Model m");
        return query.getResultList();
    }

    @Override
    public List<Model> getModelsWithCategories() {
        Query query = em.createQuery("SELECT m FROM Model m INNER JOIN m.category c ORDER BY c.categoryName, m.make, m.model ASC");
        return query.getResultList();
    }
    
    @Override
    public Model getSpecificModel(long modelId) {
        Query query = em.createQuery("SELECT m FROM Model m WHERE m.id = :inModelId");
        query.setParameter("inModelId", modelId);
        return (Model)query.getSingleResult();
    }

    @Override
    public long updateModel(Model updatedModel) {        
        Model modelToUpdate = getSpecificModel(updatedModel.getId());
        modelToUpdate.setMake(updatedModel.getMake());
        modelToUpdate.setModel(updatedModel.getModel());
        modelToUpdate.setCategory(updatedModel.getCategory());
        
        return updatedModel.getId();
    }

    @Override
    public void deleteModel(long modelId) {
        Model modelToRemove = getSpecificModel(modelId);
        em.remove(modelToRemove);
    }
    
    @Override
    public Model getModel(String make, String model) {
        Query query = em.createQuery("SELECT m FROM Model m WHERE m.make = :inMake and m.model = :inModel");
        query.setParameter("inMake", make);
        query.setParameter("inModel", model);
        return (Model)query.getSingleResult();
    }
}
