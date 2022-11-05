/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalmanagementclient;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;

/**
 *
 * @author wjahoward
 */
public class RentalRateModule {
    
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    
    public RentalRateModule() {
        
    }

    public RentalRateModule(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote) {
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
    }

    public CategorySessionBeanRemote getCategorySessionBeanRemote() {
        return categorySessionBeanRemote;
    }

    public void setCategorySessionBeanRemote(CategorySessionBeanRemote categorySessionBeanRemote) {
        this.categorySessionBeanRemote = categorySessionBeanRemote;
    }

    public RentalRateSessionBeanRemote getRentalRateSessionBeanRemote() {
        return rentalRateSessionBeanRemote;
    }

    public void setRentalRateSessionBeanRemote(RentalRateSessionBeanRemote rentalRateSessionBeanRemote) {
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
    }
    
}
