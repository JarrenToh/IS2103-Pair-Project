/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalmanagementclient.operationsmanager;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.TCustomerSessionBeanRemote;
import ejb.session.stateless.TEmployeeSessionBeanRemote;
import ejb.session.stateless.TransitDriverRecordSessionBeanRemote;

/**
 *
 * @author wjahoward
 */
public class ModelModule {

    
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private TransitDriverRecordSessionBeanRemote transitDriverRecordSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private TCustomerSessionBeanRemote tCustomerSessionBeanRemote;
    private TEmployeeSessionBeanRemote tEmployeeSessionBeanRemote;
    
    public ModelModule() {
        
    }

    public ModelModule(ModelSessionBeanRemote modelSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote, TransitDriverRecordSessionBeanRemote transitDriverRecordSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, 
            OutletSessionBeanRemote outletSessionBeanRemote, TCustomerSessionBeanRemote tCustomerSessionBeanRemote, TEmployeeSessionBeanRemote tEmployeeSessionBeanRemote) {
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.transitDriverRecordSessionBeanRemote = transitDriverRecordSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.tCustomerSessionBeanRemote = tCustomerSessionBeanRemote;
        this.tEmployeeSessionBeanRemote = tEmployeeSessionBeanRemote;
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
    
        /**
     * @return the carSessionBeanRemote
     */
    public CarSessionBeanRemote getCarSessionBeanRemote() {
        return carSessionBeanRemote;
    }

    /**
     * @param carSessionBeanRemote the carSessionBeanRemote to set
     */
    public void setCarSessionBeanRemote(CarSessionBeanRemote carSessionBeanRemote) {
        this.carSessionBeanRemote = carSessionBeanRemote;
    }

    /**
     * @return the transitDriverRecordSessionBeanRemote
     */
    public TransitDriverRecordSessionBeanRemote getTransitDriverRecordSessionBeanRemote() {
        return transitDriverRecordSessionBeanRemote;
    }

    /**
     * @param transitDriverRecordSessionBeanRemote the transitDriverRecordSessionBeanRemote to set
     */
    public void setTransitDriverRecordSessionBeanRemote(TransitDriverRecordSessionBeanRemote transitDriverRecordSessionBeanRemote) {
        this.transitDriverRecordSessionBeanRemote = transitDriverRecordSessionBeanRemote;
    }
    
       /**
     * @return the outletSessionBeanRemote
     */
    public OutletSessionBeanRemote getOutletSessionBeanRemote() {
        return outletSessionBeanRemote;
    }

    /**
     * @param outletSessionBeanRemote the outletSessionBeanRemote to set
     */
    public void setOutletSessionBeanRemote(OutletSessionBeanRemote outletSessionBeanRemote) {
        this.outletSessionBeanRemote = outletSessionBeanRemote;
    }

    /**
     * @return the tCustomerSessionBeanRemote
     */
    public TCustomerSessionBeanRemote gettCustomerSessionBeanRemote() {
        return tCustomerSessionBeanRemote;
    }

    /**
     * @param tCustomerSessionBeanRemote the tCustomerSessionBeanRemote to set
     */
    public void settCustomerSessionBeanRemote(TCustomerSessionBeanRemote tCustomerSessionBeanRemote) {
        this.tCustomerSessionBeanRemote = tCustomerSessionBeanRemote;
    }

    /**
     * @return the tEmployeeSessionBeanRemote
     */
    public TEmployeeSessionBeanRemote gettEmployeeSessionBeanRemote() {
        return tEmployeeSessionBeanRemote;
    }

    /**
     * @param tEmployeeSessionBeanRemote the tEmployeeSessionBeanRemote to set
     */
    public void settEmployeeSessionBeanRemote(TEmployeeSessionBeanRemote tEmployeeSessionBeanRemote) {
        this.tEmployeeSessionBeanRemote = tEmployeeSessionBeanRemote;
    }
    
}
