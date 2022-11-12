/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.TCustomerSessionBeanRemote;
import entity.Car;
import entity.Customer;
import java.util.Scanner;

/**
 *
 * @author jarrentoh
 */
public class CustomerServiceModule {

    private CarSessionBeanRemote carSessionBeanRemote;
    private TCustomerSessionBeanRemote tCustomerSessionBeanRemote;

    public CustomerServiceModule() {
    }

    public CustomerServiceModule(CarSessionBeanRemote carSessionBeanRemote, TCustomerSessionBeanRemote tCustomerSessionBeanRemote) {
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.tCustomerSessionBeanRemote = tCustomerSessionBeanRemote;
    }

    public void runCustomerServiceModule() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {

            System.out.println("\nWhat do you want to do?");
            System.out.println("1: Pickup Car");
            System.out.println("2: Return Car");
            System.out.println("3: Go back\n");
            response = 0;

            while (response < 1 || response > 11) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    pickUpCar();

                } else if (response == 2) {

                    returnCar();

                } else if (response == 3) {

                    break;

                } else {

                    System.out.println("Invalid option, please try again!\n");

                }

            }

            if (response == 3) {

                break;

            }
        }

        System.out.println("\nYou have exited out of Customer Service page successfully\n");
    }

    private void pickUpCar() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.print("Enter Customer ID > ");
        long customerId = scanner.nextLong();

        Customer customer = tCustomerSessionBeanRemote.retrieveCustomer(customerId);

        scanner.nextLine();

        System.out.print("\nEnter Car ID > ");
        long carId = scanner.nextLong();

        Car car = carSessionBeanRemote.getSpecificCar(carId);

        carSessionBeanRemote.updateCarCustomer(car, customer);

        System.out.println("\nCustomer ID: " + customerId + "have successfully picked up Car ID: " + carId + "\n");

    }

    private void returnCar() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.print("Enter Customer ID > ");
        long customerId = scanner.nextLong();
        scanner.nextLine();
        
        Customer customer = tCustomerSessionBeanRemote.retrieveCustomer(customerId);
        Car car = carSessionBeanRemote.getSpecificCar(customer.getCar().getCarId());
        
        carSessionBeanRemote.updateCarCustomer(car, null);

        System.out.println("\nCustomer ID: " + customerId + "have successfully return Car ID: " + car.getCarId() + "\n");


    }

}
