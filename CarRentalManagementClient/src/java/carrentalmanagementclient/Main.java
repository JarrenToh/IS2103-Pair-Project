/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalmanagementclient;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author jarrentoh
 */
public class Main {
    
    @EJB
    private static CategorySessionBeanRemote categorySessionBeanRemote;

    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(rentalRateSessionBeanRemote, categorySessionBeanRemote);
        mainApp.run();
    }
    
}
