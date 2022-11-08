/*
TODO: Need to include try-catch for each scanner.nextLine()
 */
package carrentalmanagementclient;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Category;
import entity.RentalRate;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
            System.out.println("4: Update Rental Rate");
            System.out.println("5: Delete Rental Rate");
            System.out.println("6. Go back\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    createRentalRate();
                } else if (response == 2) {
                    viewAllRentalRates();
                } else if (response == 3) {
                    viewSpecificRentalRate();
                } else if (response == 4) {
                    updateRentalRate();
                } else if (response == 5) {
                    deleteRentalRate();
                } else if (response == 6) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 6) {
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

            System.out.print("\nInput the type of the rental rate > ");
            String type = scanner.nextLine().toLowerCase();
            rr.setRentalRateType(getRentalRateType(type));

            System.out.print("\nInput the rate (per day) i.e. 24 hour period > ");
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

        System.out.println("\nRental Rate ----- Car Category ----- Start Date Time ----- End Date Time");
        for (int i = 0; i < rentalRates.size(); i++) {
            RentalRate r = rentalRates.get(i);
            String rName = r.getName();
            String c = r.getCategory().getCategoryName();
            LocalDateTime start = r.getStartDateTime();
            LocalDateTime end = r.getEndDateTime();
            System.out.println(rName + " ----- " + c + " ----- " + start + " ----- " + end);
        }
    }

    private void viewSpecificRentalRate() {
        RentalRate rentalRate = new RentalRate();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            List<RentalRate> rentalRates = this.rentalRateSessionBeanRemote.getRentalRates();

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
        System.out.println("Validity: " + rentalRate.getStartDateTime() + " ---> " + rentalRate.getEndDateTime());
    }

    private void updateRentalRate() {
        LocalDateTime newStartDateTime = null;
        LocalDateTime newEndDateTime = null;

        long rentalRateId = 0L;

        RentalRate rr = new RentalRate();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            List<RentalRate> rentalRates = this.rentalRateSessionBeanRemote.getRentalRates();

            if (rentalRates.isEmpty()) {
                System.out.println("No rental rates available.");
                return;
            }

            System.out.println("\nList of Rental Rates: ");
            for (int i = 0; i < rentalRates.size(); i++) {
                rr = rentalRates.get(i);
                System.out.println((i + 1) + ". " + rr.getName());
            }

            System.out.print("Select a rental rate to update (i.e. 1) > ");
            String rentalRate = scanner.nextLine();
            if (rentalRate.matches(GlobalRegex.NUMBER_REGEX)) {
                int rentalRateNumber = Integer.parseInt(rentalRate);
                rentalRateId = rentalRates.get(rentalRateNumber - 1).getId();
                rr.setId(rentalRateId);
            }

            break; // assume if is success
        }

        while (true) {
            System.out.println("\n\033[0;1mNOTE: If you don't want to update a particular field, leave it blank unless otherwise stated!!");
            List<Category> categories = this.categorySessionBeanRemote.getCategories();
            System.out.println("\nList of Categories: ");

            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
            }

            System.out.println("\nCurrent category: \033[0;1m" + rr.getCategory().getCategoryName());
            System.out.print("Update the number (corresponding to the category) you want to update to i.e. 1 > ");
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
            
            System.out.println("\nCurrent type: \033[0;1m" + rr.getRentalRateType());
            System.out.print("Update the type of the rental rate > ");
            String type = scanner.nextLine().toLowerCase();
            if (type.isEmpty()) {
                
            } else if (!type.equals("")) {
                rr.setRentalRateType(getRentalRateType(type));    
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

            System.out.println("\nCurrent start date time: \033[0;1m" + rr.getStartDateTime());
            System.out.print("Update the start date time of the rental rate (dd/MM/yyyy HH:mm) > ");
            String start = scanner.nextLine();
            if (start.isEmpty()) {

            } else {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    newStartDateTime = LocalDateTime.parse(start, formatter);
                    rr.setEndDateTime(newStartDateTime);
                } catch (DateTimeException ex) {
                    System.out.println("Error message occued: " + ex.getMessage());
                }
            }

            System.out.println("\nCurrent end date time: \033[0;1m" + rr.getEndDateTime());
            System.out.print("Update the end date time of the rental rate (dd/MM/yyyy HH:mm) > ");
            String end = scanner.nextLine();
            if (end.isEmpty()) {

            } else {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    newEndDateTime = LocalDateTime.parse(end, formatter);
                    rr.setEndDateTime(newEndDateTime);
                } catch (DateTimeException ex) {
                    System.out.println("Error message occued: " + ex.getMessage());
                }
            }

            long id = this.rentalRateSessionBeanRemote.updateRentalRate(rr);
            System.out.println(String.format("\nYou have updated rental rate with the id of %d", id));
            break; // TODO: assuming if is success, need to update with validation checks
        }
    }

    private void deleteRentalRate() {
        long rentalRateId = 0L;
        RentalRate rr = new RentalRate();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            List<RentalRate> rentalRates = this.rentalRateSessionBeanRemote.getRentalRates();

            if (rentalRates.isEmpty()) {
                System.out.println("No rental rates available.");
                return;
            }

            System.out.println("\nList of Rental Rates: ");
            for (int i = 0; i < rentalRates.size(); i++) {
                rr = rentalRates.get(i);
                System.out.println((i + 1) + ". " + rr.getName());
            }

            System.out.println("\nNOTE: The deletion of rental rate is irreversible!!");
            System.out.print("Select a rental rate to delete (i.e. 1) > ");
            String rentalRate = scanner.nextLine();
            if (rentalRate.matches(GlobalRegex.NUMBER_REGEX)) {
                int rentalRateNumber = Integer.parseInt(rentalRate);
                rentalRateId = rentalRates.get(rentalRateNumber - 1).getId();
            }

            if (rr.getEnabled()) {
                System.out.println("Unfortunately, you cannot delete the rental rate as it has already been used currently");
                continue;
            }

            // prompt user
            while (true) {
                System.out.print(String.format("Are you sure you want to delete rental rate of id %d (n for no, y for yes) > ", rr.getId()));
                String response = scanner.nextLine();

                if (response.equals("n")) {
                    break;
                } else if (response.equals("y")) {
                    this.rentalRateSessionBeanRemote.deleteRentalRate(rentalRateId);
                    System.out.println("\nYou have successfully deleted the rental rate");
                    break;
                }
            }
        }
    }

    private RentalRateType getRentalRateType(String rentalRateType) {
        switch (rentalRateType) {
            case "default":
                return RentalRateType.DEFAULT;
            case "promotion":
                return RentalRateType.PROMOTION;
            default:
                return RentalRateType.PEAK;
        }
    }
}
