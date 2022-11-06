/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

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
    public long createModel(Model m) {
        em.persist(m);
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
        Query query = em.createQuery("SELECT m FROM Model m INNER JOIN m.category c ORDER BY c.categoryName, m.makeAndModelName ASC");
        return query.getResultList();
    }
    
    @Override
    public long updateModel(Model m) {
        em.merge(m);
        return m.getId();
    }

    
}
