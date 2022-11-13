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
import util.enumeration.CarStatusEnum;

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
        return (Model) query.getSingleResult();
    }

    @Override
    public long updateModel(Model updatedModel) {
        Model modelToUpdate = getSpecificModel(updatedModel.getId());
        Category categoryToUpdate = em.find(Category.class, updatedModel.getCategory().getId());
        modelToUpdate.setMake(updatedModel.getMake());
        modelToUpdate.setModel(updatedModel.getModel());
        modelToUpdate.setEnabled(updatedModel.getEnabled());
        modelToUpdate.setCategory(categoryToUpdate);

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
        return (Model) query.getSingleResult();
    }

    @Override
    public boolean modelInUse(long modelId) {
        
        Query query = em.createQuery("SELECT c FROM Car c INNER JOIN c.model m INNER JOIN m.category cat INNER JOIN cat.rentalRates r WHERE c.status = :carStatus and m.id = :modelId");
        query.setParameter("carStatus", CarStatusEnum.UNAVAILABLE);
        query.setParameter("modelId", modelId);
        return !query.getResultList().isEmpty();
    }
    
    @Override
    public Model getModelByMakeAndModel(String make, String model) {
        Query query = em.createQuery("SELECT m FROM Model m WHERE m.make = :make AND m.model = :model");
        query.setParameter("make", make);
        query.setParameter("model", make);
        return (Model) query.getSingleResult();
    }

}
