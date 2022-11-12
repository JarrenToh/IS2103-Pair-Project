/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservedSessionBeanRemote;
import ejb.session.stateless.TCustomerSessionBeanRemote;
import ejb.session.stateless.TEmployeeSessionBeanRemote;
import ejb.session.stateless.TransitDriverRecordSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author jarrentoh
 */
public class Main {

    @EJB
    private static ReservedSessionBeanRemote reservedSessionBeanRemote;

    @EJB
    private static TEmployeeSessionBeanRemote tEmployeeSessionBeanRemote;

    @EJB
    private static TCustomerSessionBeanRemote tCustomerSessionBeanRemote;

    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;

    @EJB
    private static TransitDriverRecordSessionBeanRemote transitDriverRecordSessionBeanRemote;

    @EJB
    private static CarSessionBeanRemote carSessionBeanRemote;
    
    @EJB
    private static CategorySessionBeanRemote categorySessionBeanRemote;
    
    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;

    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    
    
    
    
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(rentalRateSessionBeanRemote, categorySessionBeanRemote, modelSessionBeanRemote, transitDriverRecordSessionBeanRemote, carSessionBeanRemote, outletSessionBeanRemote, tCustomerSessionBeanRemote, tEmployeeSessionBeanRemote, reservedSessionBeanRemote);
        mainApp.run();
    }
    
}
