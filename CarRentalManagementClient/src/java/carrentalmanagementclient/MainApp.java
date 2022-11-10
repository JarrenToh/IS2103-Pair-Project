/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalmanagementclient;

import carrentalmanagementclient.operationsmanager.ModelModule;
import carrentalmanagementclient.operationsmanager.ModelApp;
import carrentalmanagementclient.salesmanager.RentalRateModule;
import carrentalmanagementclient.salesmanager.RentalRateApp;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TCustomerSessionBeanRemote;
import ejb.session.stateless.TEmployeeSessionBeanRemote;
import ejb.session.stateless.TransitDriverRecordSessionBeanRemote;
import java.util.Scanner;

/**
 *
 * @author wjahoward
 */
public class MainApp {

    private CategorySessionBeanRemote categorySessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private TransitDriverRecordSessionBeanRemote transitDriverRecordSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private TCustomerSessionBeanRemote tCustomerSessionBeanRemote;
    private TEmployeeSessionBeanRemote tEmployeeSessionBeanRemote;
    
    // sub-apps/pages
    private RentalRateApp rentalRateApp;
    private ModelApp modelApp;

    // modules
    private RentalRateModule rentalRateModule;
    private ModelModule modelModule;
        
    public MainApp() {
        
    }
    
    public MainApp(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, TransitDriverRecordSessionBeanRemote transitDriverRecordSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote,
            OutletSessionBeanRemote outletSessionBeanRemote, TCustomerSessionBeanRemote tCustomerSessionBeanRemote, TEmployeeSessionBeanRemote tEmployeeSessionBeanRemote) {
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.transitDriverRecordSessionBeanRemote = transitDriverRecordSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.tCustomerSessionBeanRemote = tCustomerSessionBeanRemote;
        this.tEmployeeSessionBeanRemote = tEmployeeSessionBeanRemote;
        
    }
    
    public void run() {
        // login page
        // allow user to login and exit
        
        // set the necessary modules here after login
        rentalRateModule = new RentalRateModule(rentalRateSessionBeanRemote, categorySessionBeanRemote);
        modelModule = new ModelModule(modelSessionBeanRemote, categorySessionBeanRemote, transitDriverRecordSessionBeanRemote, carSessionBeanRemote, outletSessionBeanRemote, tCustomerSessionBeanRemote, tEmployeeSessionBeanRemote);
        
        // set the sub-apps/pages here
        rentalRateApp = new RentalRateApp(rentalRateModule);
        modelApp = new ModelApp(modelModule);
        
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
            System.out.println("2. Model & Car Management");
            System.out.println("3. Logout\n");
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
                    modelApp.runModelApp();
                }
                else if (response == 3) {
                    break;
                    
                } else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if (response == 3)
            {
                break;
                // TODO: navigate back to home page
            }
        }
        System.out.println("\nYou have logged out successfully\n");
    }
    
}
