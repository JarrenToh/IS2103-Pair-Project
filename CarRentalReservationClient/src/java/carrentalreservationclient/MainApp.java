/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import java.util.Scanner;

/**
 *
 * @author wjahoward
 */
public class MainApp {
    
    private CarSessionBeanRemote carSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    
    public MainApp() {
        
    }
    
    public MainApp(CarSessionBeanRemote carSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote) {
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
    }
    
    public void run() {
        // login page
        
        
    }
    
    public void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Hello! Car Rental Reservation Client ***\n");
//            System.out.println("Hello " + currentATMCard.getNameOnCard()+ "\n"); - TODO: need to modify this for employee
            // TODO: maybe can show current date and time via system.out.println() ?
            System.out.println("Select which platform you would like to navigate");
            System.out.println("1. Search Car");
            System.out.println("2. Reserve Car");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View Reservation Details");
            System.out.println("5. View All My Reservations");
            System.out.println("6. Logout\n");
            response = 0;
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    searchCar();
                }
                else if (response == 2)
                {
                    reserveCar();
                }
                else if (response == 3)
                {
                    cancelReservation();
                }
                else if (response == 4) 
                {
                    viewReservationDetails();
                }
                else if (response == 5)
                {
                    viewAllMyReservations();
                }
                else if (response == 6)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if (response == 6)
            {
                break;
                // TODO: navigate back to home page
            }
        }
        System.out.println("\nYou have logged out successfully\n");
    }
    
    private void searchCar() {
        // a car is available for rental if the enum status is available
    }
    
    private void reserveCar() {
        
    }
    
    private void cancelReservation() {
        
    }
    
    private void viewReservationDetails() {
        
    }
    
    private void viewAllMyReservations() {
        
    }
    
}
