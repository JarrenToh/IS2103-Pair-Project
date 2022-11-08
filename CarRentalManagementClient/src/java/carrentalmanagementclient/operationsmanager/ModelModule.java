/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalmanagementclient.operationsmanager;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;

/**
 *
 * @author wjahoward
 */
public class ModelModule {
    
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    
    public ModelModule() {
        
    }

    public ModelModule(ModelSessionBeanRemote modelSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote) {
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
    }

    public CategorySessionBeanRemote getCategorySessionBeanRemote() {
        return categorySessionBeanRemote;
    }

    public void setCategorySessionBeanRemote(CategorySessionBeanRemote categorySessionBeanRemote) {
        this.categorySessionBeanRemote = categorySessionBeanRemote;
    }

    public ModelSessionBeanRemote getModelSessionBeanRemote() {
        return modelSessionBeanRemote;
    }

    public void setModelSessionBeanRemote(ModelSessionBeanRemote modelSessionBeanRemote) {
        this.modelSessionBeanRemote = modelSessionBeanRemote;
    }
    
}
