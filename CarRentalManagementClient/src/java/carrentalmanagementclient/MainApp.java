/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalmanagementclient;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import java.util.Scanner;

/**
 *
 * @author wjahoward
 */
public class MainApp {

    private CategorySessionBeanRemote categorySessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    
    // sub-apps/pages
    private RentalRateApp rentalRateApp;

    // modules
    private RentalRateModule rentalRateModule;
    
    // entities
//    private AtmCard currentATMCard;
    
    public MainApp() {
        
    }
    
    public MainApp(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote) {
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
    }
    
    public void run() {
        // login page
        // allow user to login and exit
        
        // set the necessary modules here after login
        rentalRateModule = new RentalRateModule(rentalRateSessionBeanRemote, categorySessionBeanRemote);
        
        // set the sub-apps/pages here
        rentalRateApp = new RentalRateApp(rentalRateModule);
        
        menuMain();
    }
    
    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Hello! Car Rental Management Client Terminal ***\n");
//            System.out.println("Hello " + currentATMCard.getNameOnCard()+ "\n"); - TODO: need to modify this for employee
            // TODO: maybe can show current date and time via system.out.println() ?
            System.out.println("Select which platform you would like to navigate");
            System.out.println("1. Rental Rate");
            System.out.println("2. Model");
            System.out.println("3. Car");
            System.out.println("4. Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("5. Logout\n");
            response = 0;
            
            while(response < 1 || response > 5)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    rentalRateApp.runRentalRateApp();
                }
                else if (response == 2)
                {
                    categorySessionBeanRemote.deleteCategory();
                }
                else if (response == 5)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if (response == 5)
            {
                break;
                // TODO: navigate back to home page
            }
        }
    }
    
}
