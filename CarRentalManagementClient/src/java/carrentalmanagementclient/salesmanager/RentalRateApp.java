/*
TODO: Need to include try-catch for each scanner.nextLine()
 */
package carrentalmanagementclient.salesmanager;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Category;
import entity.RentalRate;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import util.enumeration.RentalRateType;
import util.regex.GlobalRegex;

/**
 *
 * @author wjahoward
 */
public class RentalRateApp {

    private RentalRateModule rentalRateModule;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;

    public RentalRateApp() {

    }

    public RentalRateApp(RentalRateModule rentalRateModule) {
        this.rentalRateModule = rentalRateModule;
        this.categorySessionBeanRemote = this.rentalRateModule.getCategorySessionBeanRemote();
        this.rentalRateSessionBeanRemote = this.rentalRateModule.getRentalRateSessionBeanRemote();
    }

    public void runRentalRateApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("\nWhat do you want to do?");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View Specific Rental Rate");
            System.out.println("4: Go back\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    
                    createRentalRate();
                    
                } else if (response == 2) {

                    viewAllRentalRates();

                } else if (response == 3) {

                    viewRentalDetails();

                } else if (response == 4) {
                    break;
                    
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
        System.out.println("\nYou have exited out of Rental Rate page successfully\n");
    }

    private void createRentalRate() {
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
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

            scanner.nextLine();
            System.out.print("\nInput the name of the rental rate > ");
            String name = scanner.nextLine();
            rr.setName(name);

            while (true) {

                System.out.print("\nSelect Rental Rate Type (1: Default, 2: Promotion, 3: Peak) > ");
                Integer rentalRateInt = scanner.nextInt();

                if (rentalRateInt >= 1 && rentalRateInt <= 3) {

                    rr.setRentalRateType(RentalRateType.values()[rentalRateInt - 1]);
                    break;

                } else {

                    System.out.println("Invalid option, please try again!\n");

                }
            }

            System.out.print("\nInput the rate (per day) i.e. 24 hour period > $");
            BigDecimal ratePerDay = scanner.nextBigDecimal();
            rr.setRatePerDay(ratePerDay);

            scanner.nextLine();
            System.out.print("\nInput the start date time of the rental rate (dd/MM/yyyy HH:mm) > ");
            String start = scanner.nextLine();
            try {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                startDateTime = LocalDateTime.parse(start, formatter);
                rr.setStartDateTime(startDateTime);

            } catch (DateTimeException ex) {

                System.out.println("Error message occued: " + ex.getMessage());

            }

            System.out.print("\nInput the end date time of the rental rate (dd/MM/yyyy HH:mm) > ");
            String end = scanner.nextLine();
            try {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                endDateTime = LocalDateTime.parse(end, formatter);
                rr.setEndDateTime(endDateTime);

            } catch (DateTimeException ex) {

                System.out.println("Error message occued: " + ex.getMessage());

            }

            boolean enabled = false;
            rr.setEnabled(enabled);

            long id = this.rentalRateSessionBeanRemote.createRentalRate(rr, c);

            System.out.println(String.format("\nYou have created rental rate with the id of %d", id));
            // TOOD: once successful, need to include validation as well
            break;
        }
    }

    // TODO: Cater if there is no rental rate records
    private void viewAllRentalRates() {

        List<RentalRate> rentalRates = this.rentalRateSessionBeanRemote.getRentalRatesWithCategories();

        if (rentalRates.isEmpty()) {
            System.out.println("No rental rates available.");
            return;
        }

        System.out.println("\nID ----- Rental Rate Name ----- Car Category ----- Start Date Time ----- End Date Time ----- Rental Rate Type ----- Rate Per Day");
        for (int i = 0; i < rentalRates.size(); i++) {
            RentalRate r = rentalRates.get(i);
            long rentalRateID = r.getId();
            String rName = r.getName();
            String c = r.getCategory().getCategoryName();
            LocalDateTime start = r.getStartDateTime();
            LocalDateTime end = r.getEndDateTime();
            RentalRateType rrType = r.getRentalRateType();
            BigDecimal rrPerDay = r.getRatePerDay();
            System.out.println(rentalRateID + " ----- " + rName + " ----- " + c + " ----- " + start + " ----- " + end + " ----- " + rrType + " ----- $" + rrPerDay);
        }
    }

    private void viewRentalDetails() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        System.out.print("Enter Rental Rate ID> ");
        long rentalRateId = scanner.nextLong();

        RentalRate rentalRate = rentalRateSessionBeanRemote.getSpecificRental(rentalRateId);
        System.out.println("\nID ----- Rental Rate Name ----- Car Category ----- Start Date Time ----- End Date Time ----- Rental Rate Type ----- Rate Per Day");
        System.out.println(rentalRate.getId() + " ----- " + rentalRate.getName() + " ----- " + rentalRate.getCategory().getCategoryName() + " ----- " + rentalRate.getStartDateTime() + " ----- " + rentalRate.getEndDateTime() + " ----- " + rentalRate.getRentalRateType() + " ----- $" + rentalRate.getRatePerDay());
        System.out.println("------------------------");
        System.out.println("1: Update Rental Rate");
        System.out.println("2: Delete Rental Rate");
        System.out.println("3: Back\n");
        System.out.print("> ");
        response = scanner.nextInt();

        if (response == 1) {

            doUpdateRentalRate(rentalRate);

        } else if (response == 2) {
            
            if(!rentalRateSessionBeanRemote.rentalRateInUse(rentalRate.getId())) {

                doDeleteRentalRate(rentalRate);
                
            } else {
            
                System.out.println("Unfortunately, you cannot delete the model as it has already been used currently");
                
                rentalRate.setEnabled(false);
                
                rentalRateSessionBeanRemote.updateRentalRate(rentalRate);
                
                System.out.println("System have disable Rental Rate with Id = " + rentalRate.getId());
            }
        }

    }

    private void doUpdateRentalRate(RentalRate rentalRate) {

        Scanner scanner = new Scanner(System.in);
        String input;
        Integer integerInput;
        BigDecimal bigDecimalInput;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        System.out.println("*** Update Rental Rate ***\n");
        System.out.print("Enter Rental Rate Name (blank if no change)> ");
        input = scanner.nextLine().trim();

        if (input.length() > 0) {

            rentalRate.setName(input);
        }

        while (true) {

            System.out.print("\nSelect Rental Rate Type (1: Default, 2: Promotion, 3: Peak) (negative number if no change)> ");
            integerInput = scanner.nextInt();

            if (integerInput >= 1 && integerInput <= 3) {

                rentalRate.setRentalRateType(RentalRateType.values()[integerInput - 1]);
                break;

            } else {

                System.out.println("Invalid option, please try again!\n");

            }

            if (integerInput < 0) {

                break;

            }
        }

        System.out.print("\nInput the rate (per day) i.e. 24 hour period (negative number if no change)> $");
        bigDecimalInput = scanner.nextBigDecimal();

        if (bigDecimalInput.compareTo(BigDecimal.ZERO) > 0) {

            rentalRate.setRatePerDay(bigDecimalInput);

        }

        scanner.nextLine();

        System.out.print("\nInput the start date time of the rental rate (dd/MM/yyyy HH:mm) (blank if no change)> ");
        input = scanner.nextLine();

        if (input.length() > 0) {
            try {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                startDateTime = LocalDateTime.parse(input, formatter);
                rentalRate.setStartDateTime(startDateTime);

            } catch (DateTimeException ex) {

                System.out.println("Error message occued: " + ex.getMessage());

            }
        }

        System.out.print("\nInput the end date time of the rental rate (dd/MM/yyyy HH:mm) (blank if no change)> ");
        input = scanner.nextLine();

        if (input.length() > 0) {
            try {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                endDateTime = LocalDateTime.parse(input, formatter);
                rentalRate.setEndDateTime(endDateTime);

            } catch (DateTimeException ex) {

                System.out.println("Error message occued: " + ex.getMessage());

            }
        }

        while (true) {

            System.out.print("\nEnable Rental (1: true, 2: false) (negative number if no change)> ");
            integerInput = scanner.nextInt();

            if (integerInput < 0) {

                break;

            } else if (integerInput == 1) {

                rentalRate.setEnabled(true);
                break;

            } else if (integerInput == 2) {

                rentalRate.setEnabled(false);
                break;

            } else {

                System.out.println("Invalid option, please try again!\n");

            }
        }

        rentalRateSessionBeanRemote.updateRentalRate(rentalRate);

        System.out.println(String.format("\nYou have successfully update Rental Rate with ID: %d", rentalRate.getId()));

    }

    private void doDeleteRentalRate(RentalRate rentalRate) {

        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** Delete Rental Rate ***\n");
        System.out.printf("Confirm Delete Rental Rate %s (Rental Rate Id: %d) (Enter 'Y' to Delete)> ", rentalRate.getName(), rentalRate.getId());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {

            rentalRateSessionBeanRemote.deleteRentalRate(rentalRate.getId());
            System.out.println("Rental Rate deleted successfully!\n");

        } else {
            System.out.println("Rental Rate NOT deleted!\n");
        }

    }

//    private void viewSpecificRentalRate() {
//        RentalRate rentalRate = new RentalRate();
//        Scanner scanner = new Scanner(System.in);
//
//        while (true) {
//
//            List<RentalRate> rentalRates = this.rentalRateSessionBeanRemote.getRentalRates();
//
//            if (rentalRates.isEmpty()) {
//
//                System.out.println("No rental rates available.");
//                break;
//            }
//
//            for (int i = 0; i < rentalRates.size(); i++) {
//
//                RentalRate r = rentalRates.get(i);
//                System.out.println((i + 1) + ". " + r.getName());
//
//            }
//
//            while (true) {
//
//                System.out.print("Select a rental rate to view (i.e. 1) > ");
//                int rentalRateNumber = scanner.nextInt();
//
//                if (rentalRateNumber > 0 && rentalRateNumber <= rentalRates.size()) {
//
//                    rentalRate = rentalRates.get(rentalRateNumber - 1);
//                    break; // assume if is success
//
//                } else {
//
//                    System.out.println("Invalid option, please try again!\n");
//                }
//            }
//
//            Integer input = 0;
//
//            while (true) {
//
//                System.out.println("\n-----Rental Rate Details -----");
//                System.out.println("ID: " + rentalRate.getId());
//                System.out.println("Name: " + rentalRate.getName());
//                System.out.println("Rate (Per Day): $" + rentalRate.getRatePerDay());
//                System.out.println("Validity: " + rentalRate.getStartDateTime() + " ---> " + rentalRate.getEndDateTime() + "\n\n");
//
//                System.out.print("\nSelect Options (1: Update Rental Rate, 2: Delete Rental Rate, 3: Exit) > ");
//                input = 0;
//
//                while (input < 1 || input > 2) {
//
//                    input = scanner.nextInt();
//
//                    if (input == 1) {
//
//                        updateRentalRate(rentalRate.getId());
//
//                    } else if (input == 2) {
//
//                        deleteRentalRate(rentalRate.getId());
//
//                    } else if (input == 3) {
//
//                        break;
//
//                    } else {
//
//                        System.out.println("Invalid option, please try again!\n");
//                    }
//                }
//
//                if (input == 3) {
//
//                    break;
//                }
//            }
//
//            break;
//        }
//
//    }
//
//    private void updateRentalRate(long updateRentalRateId) {
//
//        LocalDateTime newStartDateTime = null;
//        LocalDateTime newEndDateTime = null;
//
//        long rentalRateId = 0L;
//
//        RentalRate rr = new RentalRate();
//        Scanner scanner = new Scanner(System.in);
//
//        while (true) {
//            List<RentalRate> rentalRates = this.rentalRateSessionBeanRemote.getRentalRates();
//
//            if (rentalRates.isEmpty()) {
//                System.out.println("No rental rates available.");
//                return;
//            }
//
//            System.out.println("\nList of Rental Rates: ");
//            for (int i = 0; i < rentalRates.size(); i++) {
//                rr = rentalRates.get(i);
//                System.out.println((i + 1) + ". " + rr.getName());
//            }
//
//            System.out.print("Select a rental rate to update (i.e. 1) > ");
//            String rentalRate = scanner.nextLine();
//            if (rentalRate.matches(GlobalRegex.NUMBER_REGEX)) {
//                int rentalRateNumber = Integer.parseInt(rentalRate);
//                rentalRateId = rentalRates.get(rentalRateNumber - 1).getId();
//                rr.setId(rentalRateId);
//            }
//
//            break; // assume if is success
//        }
//
//        while (true) {
//            System.out.println("\n\033[0;1mNOTE: If you don't want to update a particular field, leave it blank unless otherwise stated!!");
//            List<Category> categories = this.categorySessionBeanRemote.getCategories();
//            System.out.println("\nList of Categories: ");
//
//            for (int i = 0; i < categories.size(); i++) {
//                System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
//            }
//
//            System.out.println("\nCurrent category: \033[0;1m" + rr.getCategory().getCategoryName());
//            System.out.print("Update the number (corresponding to the category) you want to update to i.e. 1 > ");
//            String categoryNumber = scanner.nextLine(); // why scanner.nextLine() is used instead of scanner.next() - we should allow the user to input empty blank spaces if don't want to update that particular field
//
//            if (categoryNumber.isEmpty()) {
//
//            } else {
//                if (categoryNumber.matches(GlobalRegex.NUMBER_REGEX)) {
//                    int categoryNumberInt = Integer.parseInt(categoryNumber);
//                    if (categoryNumberInt >= 1 && categoryNumberInt <= categories.size()) {
//                        rr.setCategory(categories.get(categoryNumberInt - 1));
//                    }
//                }
//            }
//
//            System.out.println("\nCurrent name: \033[0;1m" + rr.getName());
//            System.out.print("Update the name of the rental rate > ");
//            String newName = scanner.nextLine();
//            if (newName.isEmpty()) {
//
//            } else if (!newName.equals("")) {
//                rr.setName(newName);
//            }
//
//            System.out.println("\nCurrent type: \033[0;1m" + rr.getRentalRateType());
//            System.out.print("Update the type of the rental rate > ");
//            String type = scanner.nextLine().toLowerCase();
//            if (type.isEmpty()) {
//
//            } else if (!type.equals("")) {
//                rr.setRentalRateType(getRentalRateType(type));
//            }
//
//            System.out.println("\nCurrent rate (per day): \033[0;1m$" + rr.getRatePerDay());
//            System.out.print("Update the rate (per day) i.e. 24 hour period > ");
//            String newRatePerDay = scanner.nextLine();
//            if (newRatePerDay.isEmpty()) {
//
//            } else {
//                if (newRatePerDay.matches(GlobalRegex.DOUBLE_REGEX)) {
//                    BigDecimal newRatePerDayBD = new BigDecimal(newRatePerDay);
//                    if (newRatePerDayBD.compareTo(BigDecimal.ZERO) > 0) {
//                        rr.setRatePerDay(newRatePerDayBD);
//                    }
//                }
//            }
//
//            System.out.println("\nCurrent start date time: \033[0;1m" + rr.getStartDateTime());
//            System.out.print("Update the start date time of the rental rate (dd/MM/yyyy HH:mm) > ");
//            String start = scanner.nextLine();
//            if (start.isEmpty()) {
//
//            } else {
//                try {
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//                    newStartDateTime = LocalDateTime.parse(start, formatter);
//                    rr.setEndDateTime(newStartDateTime);
//                } catch (DateTimeException ex) {
//                    System.out.println("Error message occued: " + ex.getMessage());
//                }
//            }
//
//            System.out.println("\nCurrent end date time: \033[0;1m" + rr.getEndDateTime());
//            System.out.print("Update the end date time of the rental rate (dd/MM/yyyy HH:mm) > ");
//            String end = scanner.nextLine();
//            if (end.isEmpty()) {
//
//            } else {
//                try {
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//                    newEndDateTime = LocalDateTime.parse(end, formatter);
//                    rr.setEndDateTime(newEndDateTime);
//                } catch (DateTimeException ex) {
//                    System.out.println("Error message occued: " + ex.getMessage());
//                }
//            }
//
//            long id = this.rentalRateSessionBeanRemote.updateRentalRate(rr);
//            System.out.println(String.format("\nYou have updated rental rate with the id of %d", id));
//            break; // TODO: assuming if is success, need to update with validation checks
//        }
//    }
//
//    private void deleteRentalRate(long updateRentalRateId) {
//        long rentalRateId = 0L;
//        RentalRate rr = new RentalRate();
//        Scanner scanner = new Scanner(System.in);
//
//        while (true) {
//            List<RentalRate> rentalRates = this.rentalRateSessionBeanRemote.getRentalRates();
//
//            if (rentalRates.isEmpty()) {
//                System.out.println("No rental rates available.");
//                return;
//            }
//
//            System.out.println("\nList of Rental Rates: ");
//            for (int i = 0; i < rentalRates.size(); i++) {
//                rr = rentalRates.get(i);
//                System.out.println((i + 1) + ". " + rr.getName());
//            }
//
//            System.out.println("\nNOTE: The deletion of rental rate is irreversible!!");
//            System.out.print("Select a rental rate to delete (i.e. 1) > ");
//            String rentalRate = scanner.nextLine();
//            if (rentalRate.matches(GlobalRegex.NUMBER_REGEX)) {
//                int rentalRateNumber = Integer.parseInt(rentalRate);
//                rentalRateId = rentalRates.get(rentalRateNumber - 1).getId();
//            }
//
//            if (rr.getEnabled()) {
//                System.out.println("Unfortunately, you cannot delete the rental rate as it has already been used currently");
//                continue;
//            }
//
//            // prompt user
//            while (true) {
//                System.out.print(String.format("Are you sure you want to delete rental rate of id %d (n for no, y for yes) > ", rr.getId()));
//                String response = scanner.nextLine();
//
//                if (response.equals("n")) {
//                    break;
//                } else if (response.equals("y")) {
//                    this.rentalRateSessionBeanRemote.deleteRentalRate(rentalRateId);
//                    System.out.println("\nYou have successfully deleted the rental rate");
//                    break;
//                }
//            }
//        }
//    }
//
//    //not needed
//    private RentalRateType getRentalRateType(String rentalRateType) {
//        switch (rentalRateType) {
//            case "default":
//                return RentalRateType.DEFAULT;
//            case "promotion":
//                return RentalRateType.PROMOTION;
//            default:
//                return RentalRateType.PEAK;
//        }
//    }
}
