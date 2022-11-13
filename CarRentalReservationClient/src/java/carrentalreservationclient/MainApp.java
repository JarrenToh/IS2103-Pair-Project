/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservedSessionBeanRemote;
import ejb.session.stateless.TCustomerSessionBeanRemote;
import entity.Car;
import entity.Category;
import entity.Customer;
import entity.Model;
import entity.Outlet;
import entity.RentalRate;
import entity.Reserved;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import util.helper.Pair;
import util.enumeration.RentalRateType;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author wjahoward
 */
public class MainApp {

    private CarSessionBeanRemote carSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private TCustomerSessionBeanRemote customerSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private ReservedSessionBeanRemote reservedSessionBeanRemote;
    private LocalDateTime currentLocalDateTime;

    private Customer customer;

    public MainApp() {

    }

    public MainApp(CarSessionBeanRemote carSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, ReservedSessionBeanRemote reservedSessionBeanRemote) {
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.reservedSessionBeanRemote = reservedSessionBeanRemote;

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
            System.out.println("2. Login");
            System.out.println("3. Quit");

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    searchCar(); // both visitor and customer can do this 
                } else if (response == 2) {
                    try {
                        doLogin();
                        customerMenuMain();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Login credentials invalid");
                    }
                } else if (response == 3) {
                    break;
                }
            }

            if (response == 3) {
                break;
            }
        }
        System.out.println("\nYou have exited the Car Rental Reservation Client successfully\n");
    }

    private void customerMenuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** Hello! Car Rental Reservation Client ***\n");
            System.out.println("1. Reserve Car");
            System.out.println("2. View Reservation Details");
            System.out.println("3. View All My Reservations");
            System.out.println("4. Logout\n");

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                   reserveCar();
                } else if (response == 2) {
                   viewReservationDetails();
                } else if (response == 3) {
                   viewAllMyReservations();
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

        while (true) {
            System.out.print("\nInput the pickup date time (dd/MM/yyyy HH:mm) > ");
            pickup = scanner.nextLine();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                pickupDateTime = LocalDateTime.parse(pickup, formatter);
            } catch (DateTimeException ex) {
                System.out.println("Error message occued: " + ex.getMessage());
            }

            System.out.print("\nInput the return date time (dd/MM/yyyy HH:mm) > ");
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

            showCarsRentalFeesFromInputs(pickupDateTime, returnDateTime, pickupOutlet, returnOutlet);
            break;
        }
    }

    private void showCarsRentalFeesFromInputs(LocalDateTime pickupDateTime, LocalDateTime returnDateTime, String pickupOutlet, String returnOutlet) {
        List<Car> carsFromInputs = new ArrayList<>();
        List<BigDecimal> rentalFeesCarsFromInputs = new ArrayList<>();

        // get the reserved records from the reserved entity whether if any cars have been reserved
        List<Reserved> reservedRecords = UserHandler.getReservedRecords(reservedSessionBeanRemote);
        List<Long> carsReservedIds = reservedRecords.stream()
                .map(r -> r.getCar().getCarId())
                .collect(Collectors.toList());

        List<Car> availableCarsFromAllOutlets = UserHandler.getAvailableCars(this.carSessionBeanRemote, carsReservedIds, pickupDateTime);

        if (!availableCarsFromAllOutlets.isEmpty()) {
            System.out.println("\nAvailable Cars: ");
            Pair<List<Car>, List<BigDecimal>> carsRentalFees = getCarsWithRentalRates(availableCarsFromAllOutlets, pickupDateTime, returnDateTime);
            carsFromInputs.addAll(carsRentalFees.first());
            rentalFeesCarsFromInputs.addAll(carsRentalFees.second());
        }

        List<Car> unavailableCarsFromAllOutlets = UserHandler.getUnavailableCars(this.carSessionBeanRemote, pickupDateTime, pickupOutlet);

        if (!unavailableCarsFromAllOutlets.isEmpty()) {
            System.out.println("\nCars that are unavailable now but are available based on your pickup time: ");
            Pair<List<Car>, List<BigDecimal>> carsRentalFees = getCarsWithRentalRates(unavailableCarsFromAllOutlets, pickupDateTime, returnDateTime);
            carsFromInputs.addAll(carsRentalFees.first());
            rentalFeesCarsFromInputs.addAll(carsRentalFees.second());
        }

    }

    private Pair<List<Car>, List<BigDecimal>> getCarsWithRentalRates(List<Car> carsFromAllOutlets, LocalDateTime pickupDateTime, LocalDateTime returnDateTime) {
        List<Car> cars = new ArrayList<>();
        List<BigDecimal> returnedRentalFees = new ArrayList<>();
        HashMap<String, Integer> makeModelOutletHM = new HashMap<>();
        int index = 1;

        for (Car c : carsFromAllOutlets) {
            Category category = c.getModel().getCategory();
            Model m = c.getModel();
            String make = m.getMake();
            String model = m.getModel();
            String outlet = c.getOutlet().getAddress();

            String makeModelOutlet = make + " " + model + " " + outlet;

            if (!makeModelOutletHM.containsKey(makeModelOutlet)) { // there are cars with the same categories
                makeModelOutletHM.put(makeModelOutlet, 1);

                long numOfCarsOfCarsBasedOnMakeAndModel = UserHandler.getNumOfCarsBasedOnMakeAndModel(this.carSessionBeanRemote, make, model);

                if (numOfCarsOfCarsBasedOnMakeAndModel == 0) { // no more inventory
                    continue;
                }

                List<RentalRate> rentalRates = UserHandler.getRentalRatesByCategoryId(this.rentalRateSessionBeanRemote, category.getId());
                List<Long> rentalRatesId = rentalRates.stream().map(r -> r.getId()).collect(Collectors.toList());
                BigDecimal rentalFee = calculateTotalRentalFee(rentalRatesId, pickupDateTime, returnDateTime);
                System.out.println(index + ". " + c.getOutlet().getAddress() + " ----- " + make + " ----- " + model + " ------ " + category.getCategoryName() + " ----- $" + rentalFee + " ----- " + numOfCarsOfCarsBasedOnMakeAndModel);
                cars.add(c);
                returnedRentalFees.add(rentalFee);
                index++;
            }
        }

        return Pair.of(cars, returnedRentalFees);
    }

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

    private void reserveCar() {
        Model m = null;
        Category c = null;

        Scanner scanner = new Scanner(System.in);
        String pickup, returnT, pickupOutlet, returnOutlet, ccDetails;
        String make = "";
        String model = "";
        String category = "";

        LocalDateTime pickupDateTime = null;
        LocalDateTime returnDateTime = null;

        Integer response = 0;

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

            System.out.print("\nInput 1 for Make and Model, 2 for Category > ");
            response = scanner.nextInt();

            if (response == 1) {
                scanner.nextLine();
                System.out.print("\nInput the make > ");
                make = scanner.nextLine();

                System.out.print("\nInput the model > ");
                model = scanner.nextLine();
            } else {
                System.out.print("\nInput the category > ");
                category = scanner.nextLine();
            }

            Outlet pickupOutletAvailable = UserHandler.getOutletForPickup(this.outletSessionBeanRemote, pickupDateTime, pickupOutlet);

            if (pickupOutletAvailable == null) {
                System.out.println("Outlet is closed given the pickup time");
                break;
            }

            // validate return outlet not allowed
            if (category.equals("")) {
                // check that make and model of that category of the outlet is available
                m = UserHandler.getModelByMakeAndModel(modelSessionBeanRemote, make, model);
                // get the total numbers of cars of that model of that outlet
                int numberOfMReserved = UserHandler.getNumberOfMReservedByOutlet(reservedSessionBeanRemote, m, pickupOutlet).size();
                // get the available cars
                int numberAvailableCarsByOutlet = UserHandler.getAvailableCarsByOutlet(carSessionBeanRemote, m, pickupOutlet).size();

                if (numberOfMReserved == numberAvailableCarsByOutlet) { // no more inventory
                    // let say if i want to pick up at outlet A at 10am
                    // for those unavailable cars, get those cars that are returned back at Outlet A at 2 hours before 10am, ok ones include: 8am, 7am
                    int numberUnavailableCarsByOutlet = UserHandler.getUnavailableCarsByOutlet(carSessionBeanRemote, m, pickupOutlet, pickupDateTime).size();

                    if (numberUnavailableCarsByOutlet == 0) {
                        System.out.println("No such make/model is available based on your pickup time");
                        break;
                    }
                }

                c = m.getCategory();
            } else {
                c = UserHandler.getCategoryByCategoryName(categorySessionBeanRemote, category);
            }

            List<RentalRate> rentalRates = UserHandler.getRentalRatesByCategoryId(this.rentalRateSessionBeanRemote, c.getId());
            List<Long> rentalRatesId = rentalRates.stream().map(r -> r.getId()).collect(Collectors.toList());
            BigDecimal rentalFee = calculateTotalRentalFee(rentalRatesId, pickupDateTime, returnDateTime);

            Reserved reserved = new Reserved();
            reserved.setTotalCost(rentalFee);
            reserved.setPickUpOutlet(pickupOutlet);
            reserved.setReturnOutlet(returnOutlet);
            reserved.setRentalStartDate(pickupDateTime);
            reserved.setRentalEndDate(returnDateTime);

            if (category.equals("")) {
                reserved.setModel(m);
            } else {
                reserved.setCategory(c);
            }

            UserHandler.reserve(this.reservedSessionBeanRemote, 1L, pickupDateTime, returnDateTime, reserved);
            break;
        }

        System.out.println("You have successfully reserved a make/model or category");
    }

    private void cancelReservation(Reserved r) {
        System.out.println("The penalty amount you have to pay is as follows : $" + reservedSessionBeanRemote.CancelReservation(r.getReservedId()));
    }

    private void viewReservationDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the reservation id > ");
        Integer response = scanner.nextInt();

        Reserved r = reservedSessionBeanRemote.viewSpecificReservation(response);

        System.out.println("ID : " + r.getReservedId());
        System.out.println("Make : " + r.getModel().getMake());
        System.out.println("Model : " + r.getModel().getModel());
        System.out.println("Pickup Outlet : " + r.getPickUpOutlet());
        System.out.println("Return Outlet : " + r.getReturnOutlet());
        System.out.println("Rental Start Date : " + r.getRentalStartDate());
        System.out.println("Rental End Date : " + r.getRentalEndDate());

        System.out.println("Do you want to cancel the reservation (Y for yes, N for no) > ");
        String userCancel = scanner.nextLine();

        if (userCancel.equals("Y")) {
            cancelReservation(r);
        }

    }

    private void viewAllMyReservations() {
        List<Reserved> reservations = reservedSessionBeanRemote.viewAllReservationOfCustomer(1L);
        for (Reserved r : reservations) {
            System.out.println("ID : " + r.getReservedId());
            System.out.println("Make : " + r.getModel().getMake());
            System.out.println("Model : " + r.getModel().getModel());
            System.out.println("Pickup Outlet : " + r.getPickUpOutlet());
            System.out.println("Return Outlet : " + r.getReturnOutlet());
            System.out.println("Rental Start Date : " + r.getRentalStartDate());
            System.out.println("Rental End Date : " + r.getRentalEndDate());
            System.out.println("-----");
        }
    }

    private void doLogin() throws InvalidLoginCredentialException {

        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** POS System :: Login ***\n");
        System.out.print("Enter email> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            customer = customerSessionBeanRemote.customerLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

}
