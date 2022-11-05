/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author wjahoward
 */
@Remote
public interface CategorySessionBeanRemote {
    void createNewCategory(Category newCategory);
    List<Category> getCategories();
}