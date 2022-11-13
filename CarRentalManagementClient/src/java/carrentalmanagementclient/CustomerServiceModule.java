/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrentalmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.ReservedSessionBeanRemote;
import ejb.session.stateless.TCustomerSessionBeanRemote;
import entity.Car;
import entity.Customer;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightEnum;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author jarrentoh
 */
public class CustomerServiceModule {

    private CarSessionBeanRemote carSessionBeanRemote;
    private TCustomerSessionBeanRemote tCustomerSessionBeanRemote;
    private ReservedSessionBeanRemote reservedSessionBeanRemote;
    private Employee employee;

    public CustomerServiceModule() {
    }

    public CustomerServiceModule(CarSessionBeanRemote carSessionBeanRemote, TCustomerSessionBeanRemote tCustomerSessionBeanRemote, ReservedSessionBeanRemote reservedSessionBeanRemote, Employee employee) {
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.tCustomerSessionBeanRemote = tCustomerSessionBeanRemote;
        this.reservedSessionBeanRemote = reservedSessionBeanRemote;
        this.employee = employee;
    }

    public void runCustomerServiceModule() throws InvalidAccessRightException{
        
        if (employee.getUserRole() != EmployeeAccessRightEnum.CUSTOMERSERVICEEXECUTIVE) {

            throw new InvalidAccessRightException("\nYou don't have CUSTOMERSERVICEEXECUTIVE Right to access Customer Service Module.");
        }

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

        System.out.print("Enter Customer ID > ");
        long customerId = scanner.nextLong();

        scanner.nextLine();

        System.out.print("\nEnter Car ID > ");
        long carId = scanner.nextLong();

        Long reservedId = reservedSessionBeanRemote.pickUpCar(carId, customerId);

        if (reservedId != null) {

            System.out.println("\nCustomer ID: " + customerId + "have successfully picked up Car ID: " + carId);
            System.out.println("Reservation ID: " + reservedId + "\n");
        }

    }

    private void returnCar() {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Customer ID > ");
        long customerId = scanner.nextLong();

        scanner.nextLine();

        System.out.print("\nEnter Car ID > ");
        long carId = scanner.nextLong();

        scanner.nextLine();

        System.out.print("\nEnter Returning Outlet ID > ");
        long outletId = scanner.nextLong();

        Long reservedId = reservedSessionBeanRemote.returnCar(carId, customerId, outletId);

        if (reservedId != null) {
            System.out.println("\nCustomer ID: " + customerId + "have successfully return Car ID: " + carId + " to Outlet ID: " + outletId);
            System.out.println("Reservation Id: " + reservedId + "\n");
        }

    }

}
