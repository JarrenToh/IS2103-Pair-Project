/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Car;
import entity.Category;
import entity.Outlet;
import entity.RentalRate;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import util.enumeration.RentalRateType;

/**
 *
 * @author wjahoward
 */
public class MainApp {

    private CarSessionBeanRemote carSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;

    public MainApp() {

    }

    public MainApp(CarSessionBeanRemote carSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote) {
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
    }

    public void run() {
        // login page
        menuMain();

    }

    public void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
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

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    searchCar();
                } else if (response == 2) {
                    reserveCar();
                } else if (response == 3) {
                    cancelReservation();
                } else if (response == 4) {
                    viewReservationDetails();
                } else if (response == 5) {
                    viewAllMyReservations();
                } else if (response == 6) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 6) {
                break;
                // TODO: navigate back to home page
            }
        }
        System.out.println("\nYou have logged out successfully\n");
    }

    private void searchCar() {
        Scanner scanner = new Scanner(System.in);
        String pickup, pickupOutlet, returnT, returnOutlet;
        LocalDateTime pickupDateTime = null;
        LocalDateTime returnDateTime = null;

        while (true) {
            System.out.print("\nInput the pickup date time (dd/MM/yyyy HH:mm) > ");
            pickup = scanner.nextLine();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                pickupDateTime = LocalDateTime.parse(pickup, formatter);
            } catch (DateTimeException ex) {
                System.out.println("Error message occued: " + ex.getMessage());
            }

            System.out.print("\nInput the return time (dd/MM/yyyy HH:mm) > ");
            returnT = scanner.nextLine();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                returnDateTime = LocalDateTime.parse(returnT, formatter);
            } catch (DateTimeException ex) {
                System.out.println("Error message occued: " + ex.getMessage());
            }

            System.out.print("\nInput the pickup outlet > ");
            pickupOutlet = scanner.nextLine();

            System.out.print("\nInput the return outlet > ");
            returnOutlet = scanner.nextLine();

            getCarsFromInputs(pickupDateTime, returnDateTime, pickupOutlet, returnOutlet);
            
            break;
        }

        // a car is available for rental if the enum status is available
        // need to get the inputs from the user and filter them accordingly
        // need to display the rental rate for the particular car
    }

    private void getCarsFromInputs(LocalDateTime pickupDateTime, LocalDateTime returnDateTime, String pickupOutlet, String returnOutlet) {
        // assume need to have a minimum of 5 cars for each outlet

        // check whether the outlet can be returned based from the return time
        List<Outlet> outletsForPickAndReturn = getOutletsForPickAndReturn(pickupDateTime, returnDateTime, returnOutlet);

        if (outletsForPickAndReturn.isEmpty()) {
            System.out.println("No available outlet for return at your timing choice");
            return;
        }

        for (Outlet o : outletsForPickAndReturn) {
            List<Car> carsFromOutlet = getCarsByOutletId(o.getOutletId(), pickupDateTime);
            System.out.println("\nAvailable cars: ");
            System.out.println("License Plate Number ----- Make ----- Model ----- Outlet ----- Total Rental Rate");
            for (Car c : carsFromOutlet) {
                Category category = c.getModel().getCategory();
                List<RentalRate> rentalRates = getRentalRatesByCategoryId(category.getId());
                List<Long> rentalRatesId = rentalRates.stream().map(r -> r.getId()).collect(Collectors.toList());
                BigDecimal rentalFee = calculateTotalRentalFee(rentalRatesId, pickupDateTime, returnDateTime);
                System.out.println(c.getLicensePlateNumber() + " ----- " + c.getModel().getMake() + " ------ " + c.getModel().getModel() + " ----- " + c.getOutlet().getAddress() + " ----- $" + rentalFee);
            }
        }
    }

    private List<Outlet> getOutletsForPickAndReturn(LocalDateTime pickupDateTime, LocalDateTime returnDateTime, String returnOutlet) {
        return this.outletSessionBeanRemote.getOutletWithPickAndReturnTime(pickupDateTime.toLocalTime(), returnDateTime.toLocalTime(), returnOutlet);
    }

    private List<Car> getCarsByOutletId(long outletId, LocalDateTime pickupDateTime) {
        return this.carSessionBeanRemote.getCarsByOutletId(outletId, pickupDateTime);
    }

    private List<RentalRate> getRentalRatesByCategoryId(long categoryId) {
        return this.rentalRateSessionBeanRemote.getRentalRatesByCategoryId(categoryId);
    }

    /*
    1	Default 	Default
    2	Default and Promotion	Promotion
    3	Default and Peak	Peak
    4	Default, Promotion and Peak	Promotion
    
    promotion > peak > default
    
    calculate based on the hourly rate
     */
    private BigDecimal calculateTotalRentalFee(List<Long> rentalRatesId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime) {
        BigDecimal totalRentalFee = new BigDecimal(0);
        LocalDateTime tempPickupDateTime = pickupDateTime;
        
        while (tempPickupDateTime.compareTo(returnDateTime) != 1) {
            // check for promotion, peak and default based on a particular date
            BigDecimal promotionPrice = getRentalRatePriceByDateTimeAndType(rentalRatesId, tempPickupDateTime, RentalRateType.PROMOTION);
            if (promotionPrice != null) {
                totalRentalFee = totalRentalFee.add(promotionPrice);
                tempPickupDateTime = tempPickupDateTime.plusDays(1);
                continue;
            }

            BigDecimal peakPrice = getRentalRatePriceByDateTimeAndType(rentalRatesId, tempPickupDateTime, RentalRateType.PEAK);
            if (peakPrice != null) {
                totalRentalFee = totalRentalFee.add(peakPrice);
                tempPickupDateTime = tempPickupDateTime.plusDays(1);
                continue;
            }

            BigDecimal defaultPrice = getRentalRatePriceByDateTimeAndType(rentalRatesId, tempPickupDateTime, RentalRateType.DEFAULT);
            if (defaultPrice != null) {
                totalRentalFee = totalRentalFee.add(defaultPrice);
                tempPickupDateTime = tempPickupDateTime.plusDays(1);
            }
        }

        return totalRentalFee;
    }

    private BigDecimal getRentalRatePriceByDateTimeAndType(List<Long> rentalRatesId, LocalDateTime tempDateTime, RentalRateType rentalRateType) {
        RentalRate rr = this.rentalRateSessionBeanRemote.getRentalRatePriceByDateTimeAndType(rentalRatesId, tempDateTime, rentalRateType);
        if (rr != null) {
            return rr.getRatePerDay();
        }

        return null;
    }

    // 05/12/2022 12:00, 07/12/2022 12:00, Outlet C, Outlet C
    private void reserveCar() {

    }

    private void cancelReservation() {

    }

    private void viewReservationDetails() {

    }

    private void viewAllMyReservations() {

    }

}
