/*
TODO: Need to include try-catch for each scanner.next()
*/
package carrentalmanagementclient;

import entity.Category;
import entity.RentalRate;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.regex.GlobalRegex;

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
                else if (response == 4)
                {
                    updateRentalRate();
                } else if (response == 5)
                {
                    deleteRentalRate();
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
            }
        }
        System.out.println("\nYou have exited out of Rental Rate page successfully\n");
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
            
            boolean enabled = false;
            rr.setEnabled(enabled);
            
            rr.setCategory(c);
            
            long id = rentalRateModule.getRentalRateSessionBeanRemote().createRentalRate(rr);
            System.out.println(String.format("\nYou have created rental rate with the id of %d", id));
            // TOOD: once successful, need to include validation as well
            break;
        }
    }
    
    // TODO: Cater if there is no rental rate records
    private void viewAllRentalRates() {
        List<RentalRate> rentalRates = rentalRateModule.getRentalRateSessionBeanRemote().getRentalRatesWithCategories();
        
        if (rentalRates.isEmpty()) {
                System.out.println("No rental rates available.");
                return;
        }
        
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
            
            if (rentalRates.isEmpty()) {
                System.out.println("No rental rates available.");
                return;
            }
            
            for (int i = 0; i < rentalRates.size(); i++) {
                RentalRate r = rentalRates.get(i);
                System.out.println((i + 1) + ". " + r.getName());
            }   
            System.out.print("Select a rental rate to view (i.e. 1) > ");
            int rentalRateNumber = scanner.nextInt();
            
            rentalRate = rentalRates.get(rentalRateNumber - 1);
            break; // assume if is success
        }
        
        System.out.println("\n-----Rental Rate Details -----");
        System.out.println("ID: " + rentalRate.getId());
        System.out.println("Name: " + rentalRate.getName());
        System.out.println("Rate (Per Day): $" + rentalRate.getRatePerDay());
        System.out.println("Valid until: " + rentalRate.getValidityPeriod());
    }
    
    private void updateRentalRate() {
        Date validityPeriodDate = new Date();
        RentalRate rr = new RentalRate();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            List<RentalRate> rentalRates = rentalRateModule.getRentalRateSessionBeanRemote().getRentalRates();
            
            if (rentalRates.isEmpty()) {
                System.out.println("No rental rates available.");
                return;
            }
            
            for (int i = 0; i < rentalRates.size(); i++) {
                rr = rentalRates.get(i);
                System.out.println((i + 1) + ". " + rr.getName());
            }   
            
            System.out.print("Select a rental rate to update (i.e. 1) > ");
            String rentalRate = scanner.next();
            if (rentalRate.matches(GlobalRegex.NUMBER_REGEX)) {
               int rentalRateNumber = Integer.parseInt(rentalRate);
               rr = rentalRates.get(rentalRateNumber - 1);       
            }
         
            break; // assume if is success
        }
        
        while (true) {
            System.out.println("\n\033[0;1mNOTE: If you don't want to update a particular field, leave it blank unless otherwise stated!!");
            List<Category> categories = rentalRateModule.getCategorySessionBeanRemote().getCategories();
            System.out.println("\nList of Categories: ");
                    
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
            }
        
            System.out.println("\nCurrent category: \033[0;1m" + rr.getCategory().getCategoryName());
            System.out.print("Update the number (corresponding to the category) you want to update to i.e. 1 > ");
            scanner.nextLine();
            String categoryNumber = scanner.nextLine(); // why scanner.nextLine() is used instead of scanner.next() - we should allow the user to input empty blank spaces if don't want to update that particular field
            
            if (categoryNumber.isEmpty()) {
                
            } else {
                if (categoryNumber.matches(GlobalRegex.NUMBER_REGEX)) {
                    int categoryNumberInt = Integer.parseInt(categoryNumber);
                    if (categoryNumberInt >= 1 && categoryNumberInt <= categories.size()) {
                        rr.setCategory(categories.get(categoryNumberInt - 1));    
                    }
                }
            }

            System.out.println("\nCurrent name: \033[0;1m" + rr.getName());
            System.out.print("Update the name of the rental rate > ");
            String newName = scanner.nextLine();
            if (newName.isEmpty()) {
                
            } else if (!newName.equals("")) {
                rr.setName(newName);    
            }
            
            System.out.println("\nCurrent rate (per day): \033[0;1m$" + rr.getRatePerDay());
            System.out.print("Update the rate (per day) i.e. 24 hour period > ");
            String newRatePerDay = scanner.nextLine();
            if (newRatePerDay.isEmpty()) {
                
            } else {
                if (newRatePerDay.matches(GlobalRegex.DOUBLE_REGEX)) {
                   BigDecimal newRatePerDayBD = new BigDecimal(newRatePerDay);
                    if (newRatePerDayBD.compareTo(BigDecimal.ZERO) > 0) {
                        rr.setRatePerDay(newRatePerDayBD);
                    } 
                }
            }
            
            System.out.println("\nCurrent validity period: \033[0;1m" + rr.getValidityPeriod());
            System.out.print("Update the validity period of the rental rate (dd/mm/yyyy) > ");
            String newValidityPeriod = scanner.nextLine();
            if (newValidityPeriod.isEmpty()) {
                
            } else {
                try
                {
                    SimpleDateFormat dateFor = new SimpleDateFormat("dd/MM/yyyy");
                    validityPeriodDate = dateFor.parse(newValidityPeriod);
                    rr.setValidityPeriod(validityPeriodDate);
                } catch (java.text.ParseException ex) {
                    System.out.println("Error message occued: " + ex.getMessage());
                }    
            }
            
            long id = rentalRateModule.getRentalRateSessionBeanRemote().updateRentalRate(rr);
            System.out.println(String.format("\nYou have updated rental rate with the id of %d", id));
            break; // TODO: assuming if is success, need to update with validation checks
        }
    }
    
    private void deleteRentalRate()
    {
        RentalRate rr = new RentalRate();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            List<RentalRate> rentalRates = rentalRateModule.getRentalRateSessionBeanRemote().getRentalRates();
            for (int i = 0; i < rentalRates.size(); i++) {
                RentalRate r = rentalRates.get(i);
                System.out.println((i + 1) + ". " + r.getName());
            }   
            System.out.println("NOTE: The deletion of rental rate is irreversible!!");
            System.out.print("Select a rental rate to delete (i.e. 1) > ");
            String rentalRate = scanner.next();
            if (rentalRate.matches(GlobalRegex.NUMBER_REGEX)) {
               int rentalRateNumber = Integer.parseInt(rentalRate);
               rr = rentalRates.get(rentalRateNumber - 1);       
            }
            
            if (rr.getEnabled()) {
                System.out.println("Unfortunately, you cannot delete the rental rate as it has already been used currently");
                continue;
            }
            
            // prompt user
            while (true) {
                System.out.print(String.format("Are you sure you want to delete rental rate of id %d (n for no, y for yes) > ", rr.getId()));
                String response = scanner.next();
                
                if (response.equals("n")) {
                    break;
                } else if (response.equals("y")) {
                    rentalRateModule.getRentalRateSessionBeanRemote().deleteRentalRate(rr);
                    System.out.println("You have successfully deleted the rental rate");
                    break;
                }
            }
        }
    }
}
