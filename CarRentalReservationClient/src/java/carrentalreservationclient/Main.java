/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author jarrentoh
 */
public class Main {
    
    @EJB
    private static CarSessionBeanRemote carSessionBeanRemote;

    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;
    
    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(carSessionBeanRemote, outletSessionBeanRemote, rentalRateSessionBeanRemote);
        mainApp.run();
    }
    
}
