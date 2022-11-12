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
import entity.Model;
import entity.Outlet;
import entity.RentalRate;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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
    private LocalDateTime currentLocalDateTime;

    public MainApp() {

    }

    public MainApp(CarSessionBeanRemote carSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote) {
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.currentLocalDateTime = LocalDateTime.now();
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
            System.out.println("Hello World " + this.currentLocalDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            System.out.println("Select which platform you would like to navigate");
            System.out.println("1. Search Car");
            // if is customer, view the remaining points below
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
                    searchCar(); // both visitor and customer can do this 
                } else if (response == 2) {
                    searchCar(); // here onwards only allow customer
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
            }
        }
        System.out.println("\nYou have logged out successfully\n");
    }

    /*
    user can type in the pickup outlet and return outlet 
    
    - to allow car to be rented out, have to met the following conditions:
    1. available, the pickup outlet has to be opened given the pickup time, and the returned outlet has to be opened given the return time
    2. unavailable (rented out), with a catch that if I want to rent a car from a particular outlet i.e. Outlet C but there's no cars available at Outlet C, obtain the cars from other outlets that's at least 2 hours from the return time of the rented car
    
    - if don't have, show the cars from other outlets
     */
    private void searchCar() {
        Scanner scanner = new Scanner(System.in);
        String pickup, pickupOutlet, returnT, returnOutlet;
        LocalDateTime pickupDateTime = null;
        LocalDateTime returnDateTime = null;

        String role = "customer"; // need to change this later

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

            List<Car> carsFromInputs = getCarsFromInputs(pickupDateTime, returnDateTime, pickupOutlet, returnOutlet);

//            if (role.equals("visitor")) {
//                break;
//            } else {
//                System.out.print("\nDo you want to proceed reservation (n for no, y for yes) > ");
//                String proceedWithReservation = scanner.nextLine();
//                if (proceedWithReservation.equals("y")) {
//                    reserveCar(carsFromInputs, pickupDateTime, returnDateTime, pickupOutlet, returnOutlet);    
//                }
//            }
        }
    }

    private List<Car> getCarsFromInputs(LocalDateTime pickupDateTime, LocalDateTime returnDateTime, String pickupOutlet, String returnOutlet) {
        List<Car> carsFromInputs = new ArrayList<>();

        List<Car> availableCarsFromAllOutlets = UserHandler.getAvailableCars(this.carSessionBeanRemote, pickupDateTime);

        if (!availableCarsFromAllOutlets.isEmpty()) {
            System.out.println("\nAvailable Cars: ");
            carsFromInputs.addAll(getCars(availableCarsFromAllOutlets, pickupDateTime, returnDateTime));
        }

        List<Car> unavailableCarsFromAllOutlets = UserHandler.getUnavailableCars(this.carSessionBeanRemote, pickupDateTime, pickupOutlet);

        if (!unavailableCarsFromAllOutlets.isEmpty()) {
            System.out.println("\nCars that are unavailable now but are available based on your pickup time: ");
            carsFromInputs.addAll(getCars(unavailableCarsFromAllOutlets, pickupDateTime, returnDateTime));
        }

        return carsFromInputs;
    }

    private List<Car> getCars(List<Car> carsFromAllOutlets, LocalDateTime pickupDateTime, LocalDateTime returnDateTime) {
        List<Car> cars = new ArrayList<>();
        HashMap<String, Integer> categoryHM = new HashMap<>();
        
        for (Car c : carsFromAllOutlets) {
            Category category = c.getModel().getCategory();

            if (!categoryHM.containsKey(category.getCategoryName())) { // there are cars with the same categories
                categoryHM.put(category.getCategoryName(), 1);

                Model m = c.getModel();
                String make = m.getMake();
                String model = m.getModel();

                long numOfCarsOfCarsBasedOnMakeAndModel = UserHandler.getNumOfCarsBasedOnMakeAndModel(this.carSessionBeanRemote, make, model);

                if (numOfCarsOfCarsBasedOnMakeAndModel == 0) { // no more inventory
                    continue;
                }

                List<RentalRate> rentalRates = UserHandler.getRentalRatesByCategoryId(this.rentalRateSessionBeanRemote, category.getId());
                List<Long> rentalRatesId = rentalRates.stream().map(r -> r.getId()).collect(Collectors.toList());
                BigDecimal rentalFee = calculateTotalRentalFee(rentalRatesId, pickupDateTime, returnDateTime);
                System.out.println(c.getOutlet().getAddress() + " ----- " + make + " ----- " + model + " ------ " + category.getCategoryName() + " ----- $" + rentalFee + " ----- " + numOfCarsOfCarsBasedOnMakeAndModel);
                cars.add(c);
            }
        }
        
        return cars;
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
            BigDecimal promotionPrice = UserHandler.getRentalRatePriceByDateTimeAndType(this.rentalRateSessionBeanRemote, rentalRatesId, tempPickupDateTime, RentalRateType.PROMOTION);
            if (promotionPrice != null) {
                totalRentalFee = totalRentalFee.add(promotionPrice);
                tempPickupDateTime = tempPickupDateTime.plusDays(1);
                continue;
            }

            BigDecimal peakPrice = UserHandler.getRentalRatePriceByDateTimeAndType(this.rentalRateSessionBeanRemote, rentalRatesId, tempPickupDateTime, RentalRateType.PEAK);
            if (peakPrice != null) {
                totalRentalFee = totalRentalFee.add(peakPrice);
                tempPickupDateTime = tempPickupDateTime.plusDays(1);
                continue;
            }

            BigDecimal defaultPrice = UserHandler.getRentalRatePriceByDateTimeAndType(this.rentalRateSessionBeanRemote, rentalRatesId, tempPickupDateTime, RentalRateType.DEFAULT);
            if (defaultPrice != null) {
                totalRentalFee = totalRentalFee.add(defaultPrice);
                tempPickupDateTime = tempPickupDateTime.plusDays(1);
            }
        }

        return totalRentalFee;
    }

    private void reserveCar(List<Car> cars, LocalDateTime pickupDateTime, LocalDateTime returnDateTime, String pickupOutlet, String returnOutlet) {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.print("\nSelect the car you would like to reserve (i.e. 1) > ");
            response = scanner.nextInt();

            if (response < 1 || response > cars.size()) {
                // throw exception
                continue;
            }

            Car car = cars.get(response - 1);

            // check for credit card details
            // if don't have, allow user to input cc details
            // need to update the status of the car to be UNAVAILABLE, as well as the rentalStartDate and rentalEndDate
            break;
        }

        System.out.println("You have successfully reserved a car");
    }

    private void cancelReservation() {

    }

    private void viewReservationDetails() {

    }

    private void viewAllMyReservations() {

    }

}
