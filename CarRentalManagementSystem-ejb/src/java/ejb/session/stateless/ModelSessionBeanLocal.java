/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.Model;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wjahoward
 */
@Local
public interface ModelSessionBeanLocal {

    long createModel(Model m, Category c);

    List<Model> getModels();

    List<Model> getModelsWithCategories();

    Model getSpecificModel(long modelId);

    long updateModel(Model m);

    void deleteModel(long modelId);

    Model getModel(String make, String model);

    boolean modelInUse(long modelId);

    Model getModelByMakeAndModel(String make, String model);
}
