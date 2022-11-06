/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wjahoward
 */
@Local
public interface ModelSessionBeanLocal {
    long createModel(Model m);
    List<Model> getModels();
    List<Model> getModelsWithCategories();
    long updateModel(Model m);
}
