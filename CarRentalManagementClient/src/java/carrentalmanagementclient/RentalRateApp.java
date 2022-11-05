/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalmanagementclient;

import entity.Category;
import entity.RentalRate;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author wjahoward
 */
public class RentalRateApp {
    
    private RentalRateModule rentalRateModule;

    public RentalRateApp() {
        
    }
    
    public RentalRateApp(RentalRateModule rentalRateModule) {
        this.rentalRateModule = rentalRateModule;
    }
    
    public void runRentalRateApp() { 
       Scanner scanner = new Scanner(System.in);
       Integer response = 0;
        
       while(true)
        {
            System.out.println("\nWhat do you want to do?");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View Specific Rental Rate");
            System.out.println("4: Update Rental Rate");
            System.out.println("5: Delete Rental Rate");
            System.out.println("6. Go back\n");
            response = 0;
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    createRentalRate();
                }
                else if (response == 2) 
                {
                    viewAllRentalRates();
                }
                else if (response == 3) 
                {
                    viewSpecificRentalRate();
                }
                else if (response == 6) {
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
        System.out.println("You have exited out of Rental Rate page successfully");
    }
    
    private void createRentalRate() {
        Date validityPeriodDate = new Date();
        RentalRate rr = new RentalRate();
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        List<Category> categories = rentalRateModule.getCategorySessionBeanRemote().getCategories();
        System.out.println("\nList of Categories: ");
                    
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
        }
        
        while (true) {
            System.out.print("\nSelect which category do you want to include rental rate i.e. 1 > ");
            response = scanner.nextInt();
            
            // TODO: include validation for once then just copy paste the tempalte for the subsequent ones
            Category c = categories.get(response - 1); // assuming if is the right input
            
            System.out.print("\nInput the name of the rental rate > ");
            String name = scanner.next();
            rr.setName(name);
            
            System.out.print("\nInput the rate (per day) i.e. 24 hour period > ");
            BigDecimal ratePerDay = scanner.nextBigDecimal();
            rr.setRatePerDay(ratePerDay);
            
            System.out.print("\nInput the validity period of the rental rate (dd/mm/yyyy) > ");
            String validityPeriod = scanner.next();
            try
            {
                SimpleDateFormat dateFor = new SimpleDateFormat("dd/MM/yyyy");
                validityPeriodDate = dateFor.parse(validityPeriod);
                rr.setValidityPeriod(validityPeriodDate);
            } catch (java.text.ParseException ex) {
                System.out.println("Error message occued: " + ex.getMessage());
            }
            
            // TODO: not sure if need check. need check
            boolean enabled = true;
            rr.setEnabled(enabled);
            
            rr.setCategory(c);
            
            long id = rentalRateModule.getRentalRateSessionBeanRemote().createRentalRate(rr, c);
            System.out.println(String.format("\nYou have created rental rate with the id of %d", id));
            // once successful
            break;
        }
    }
    
    private void viewAllRentalRates() {
        List<RentalRate> rentalRates = rentalRateModule.getRentalRateSessionBeanRemote().getRentalRatesWithCategories();
        System.out.println("\nCar Category ----- Validity Period");
        for (int i = 0; i < rentalRates.size(); i++) {
            RentalRate r = rentalRates.get(i);
            System.out.println(r.getCategory().getCategoryName() + " ----- " + r.getValidityPeriod());
        }
    }
    
    private void viewSpecificRentalRate()
    {
        RentalRate rentalRate = new RentalRate();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            List<RentalRate> rentalRates = rentalRateModule.getRentalRateSessionBeanRemote().getRentalRates();
            for (int i = 0; i < rentalRates.size(); i++) {
                RentalRate r = rentalRates.get(i);
                System.out.println((i + 1) + ". " + r.getName());
            }   
            System.out.print("Select a rental rate (i.e. 1) > ");
            int rentalRateNumber = scanner.nextInt();
            
            rentalRate = rentalRates.get(rentalRateNumber - 1);
            break; // assume if is success
        }
        
        System.out.println("\n-----Rental Rate Details -----");
        System.out.println("ID: " + rentalRate.getId());
        System.out.println("Name: " + rentalRate.getName());
        System.out.println("Rare Per Day: $" + rentalRate.getRatePerDay());
        System.out.println("Valid until: " + rentalRate.getValidityPeriod());
    }
}
