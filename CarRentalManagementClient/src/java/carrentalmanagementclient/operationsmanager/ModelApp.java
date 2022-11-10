/*
TODO: Need to include try-catch for each scanner.next()
 */
package carrentalmanagementclient.operationsmanager;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.TCustomerSessionBeanRemote;
import ejb.session.stateless.TEmployeeSessionBeanRemote;
import ejb.session.stateless.TransitDriverRecordSessionBeanRemote;
import entity.Car;
import entity.Category;
import entity.Model;
import entity.Outlet;
import entity.TransitDriverRecord;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import util.enumeration.CarStatusEnum;
import util.enumeration.LocationEnum;
import util.regex.GlobalRegex;

/**
 *
 * @author wjahoward
 */
public class ModelApp {

    private ModelModule modelModule;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private TransitDriverRecordSessionBeanRemote transitDriverRecordSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private TCustomerSessionBeanRemote tCustomerSessionBeanRemote;
    private TEmployeeSessionBeanRemote tEmployeeSessionBeanRemote;

    public ModelApp() {

    }

    public ModelApp(ModelModule modelModule) {
        this.modelModule = modelModule;
        this.categorySessionBeanRemote = this.modelModule.getCategorySessionBeanRemote();
        this.modelSessionBeanRemote = this.modelModule.getModelSessionBeanRemote();
        this.carSessionBeanRemote = this.modelModule.getCarSessionBeanRemote();
        this.transitDriverRecordSessionBeanRemote = this.modelModule.getTransitDriverRecordSessionBeanRemote();
        this.outletSessionBeanRemote = this.modelModule.getOutletSessionBeanRemote();
        this.tCustomerSessionBeanRemote = this.modelModule.gettCustomerSessionBeanRemote();
        this.tEmployeeSessionBeanRemote = this.modelModule.gettEmployeeSessionBeanRemote();
    }

    public void runModelApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {

            System.out.println("\nWhat do you want to do?");
            System.out.println("1: Create Model");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model");
            System.out.println("4: Delete Model");
            System.out.println("-----------------------");
            System.out.println("5: Create New Car");
            System.out.println("6: View All Car");
            System.out.println("-----------------------");
            System.out.println("7: View Transit Driver Dispatch Record");
            System.out.println("8: Assign Transit Driver");
            System.out.println("9: Update Transit As Completed");
            System.out.println("-----------------------");
            System.out.println("10: Go back\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {

                    createModel();

                } else if (response == 2) {

                    viewAllModels();

                } else if (response == 3) {

                    updateModel();

                } else if (response == 4) {

                    deleteModel();

                } else if (response == 5) {

                    createNewCar();

                } else if (response == 6) {

                    viewAllCar();

                } else if (response == 7) {

                    viewTransitDriverDispatchRecords();

                } else if (response == 8) {

                    assignTransitDriver();

                } else if (response == 9) {

                    updateTransitAsCompleted();

                } else if (response == 10) {

                    break;

                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 5) {
                break;
            }
        }
        System.out.println("\nYou have exited out of Model page successfully\n");
    }

    private void createModel() {
        Model m = new Model();

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        List<Category> categories = this.categorySessionBeanRemote.getCategories();
        System.out.println("\nList of Categories: ");

        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
        }

        while (true) {
            System.out.print("\nSelect which category do you want to include model i.e. 1 > ");
            response = scanner.nextInt();

            // TODO: include validation for once then just copy paste the tempalte for the subsequent ones
            Category c = categories.get(response - 1); // assuming if is the right input

            scanner.nextLine();
            System.out.print("\nInput the make name > ");
            String make = scanner.nextLine();
            m.setMake(make);

            System.out.print("\nInput the model name > ");
            String model = scanner.nextLine();
            m.setModel(model);

            long id = this.modelSessionBeanRemote.createModel(m, c);
            System.out.println(String.format("\nYou have created model with the id of %d", id));
            // TOOD: once successful, need to include validation as well
            break;
        }
    }

    private void viewAllModels() {
        List<Model> models = this.modelSessionBeanRemote.getModelsWithCategories();

        if (models.isEmpty()) {
            System.out.println("No models available.");
            return;
        }

        System.out.println("\n ID ----- Make ----- Model ----- Category ");
        for (int i = 0; i < models.size(); i++) {
            Model m = models.get(i);
            long modelId = m.getId();
            String make = m.getMake();
            String model = m.getModel();
            String category = m.getCategory().getCategoryName();

            System.out.println(modelId + " ----- " + make + " ----- " + model + " ----- " + category);
        }
    }

    private void updateModel() {
        Model m;
        Scanner scanner = new Scanner(System.in);
        Integer integerInput;

        while (true) {
            List<Model> models = this.modelSessionBeanRemote.getModels();

            if (models.isEmpty()) {
                System.out.println("No models available.");
                break;
            }

            System.out.println("\nList of Models: ");
            for (int i = 0; i < models.size(); i++) {
                m = models.get(i);
                System.out.println((i + 1) + ". " + m.getMake());
            }

            while (true) {
                System.out.print("\nSelect a model to update (i.e. 1) > ");
                integerInput = scanner.nextInt();
                if (integerInput > 0 && integerInput <= models.size()) {

                    m = models.get(integerInput);
                    break;

                } else {

                    System.out.println("Invalid option, please try again!\n");

                }
            }

            doUpdateModel(m);
            break; // assume if is success
        }

    }

    private void doUpdateModel(Model model) {

        Scanner scanner = new Scanner(System.in);
        String input;
        Integer integerInput;

        System.out.println("*** Update Model ***\n");

        System.out.println("\nNOTE: If you don't want to update a particular field, leave it blank unless otherwise stated!!");
        List<Category> categories = this.categorySessionBeanRemote.getCategories();
        System.out.println("\nList of Categories: ");

        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
        }

        System.out.print("\nCurrent category: \033[0;1m" + model.getCategory().getCategoryName());
        System.out.print("\nUpdate the number (corresponding to the category) you want to update to i.e. 1 > ");
        scanner.nextLine();
        String categoryNumber = scanner.nextLine(); // why scanner.nextLine() is used instead of scanner.next() - we should allow the user to input empty blank spaces if don't want to update that particular field

        if (categoryNumber.isEmpty()) {

        } else {
            if (categoryNumber.matches(GlobalRegex.NUMBER_REGEX)) {
                int categoryNumberInt = Integer.parseInt(categoryNumber);
                if (categoryNumberInt >= 1 && categoryNumberInt <= categories.size()) {
                    model.setCategory(categories.get(categoryNumberInt - 1));
                }
            }
        }

        System.out.print("Enter Make (blank if no change)> ");
        input = scanner.nextLine().trim();

        if (input.length() > 0) {

            model.setMake(input);
        }

        System.out.print("Enter Model (blank if no change)> ");
        input = scanner.nextLine().trim();

        if (input.length() > 0) {

            model.setModel(input);
        }

        while (true) {

            System.out.print("\nEnable Make and Model (1: true, 2: false) (negative number if no change)> ");
            integerInput = scanner.nextInt();

            if (integerInput < 0) {

                break;

            } else if (integerInput == 1) {

                model.setEnabled(true);
                break;

            } else if (integerInput == 2) {

                model.setEnabled(false);
                break;

            } else {

                System.out.println("Invalid option, please try again!\n");

            }
        }

        modelSessionBeanRemote.updateModel(model);
        System.out.println(String.format("\nYou have successfully update Make and Model with ID: %d", model.getId()));

    }

    private void deleteModel() {

        Model m;
        Scanner scanner = new Scanner(System.in);
        Integer integerInput;

        while (true) {
            List<Model> models = this.modelSessionBeanRemote.getModels();

            if (models.isEmpty()) {
                System.out.println("No models available.");
                break;
            }

            System.out.println("\nList of Models: ");
            for (int i = 0; i < models.size(); i++) {
                m = models.get(i);
                System.out.println((i + 1) + ". " + m.getMake());
            }

            while (true) {
                System.out.print("\nSelect a model to delete (i.e. 1) > ");
                integerInput = scanner.nextInt();
                if (integerInput > 0 && integerInput <= models.size()) {

                    m = models.get(integerInput);
                    break;

                } else {

                    System.out.println("Invalid option, please try again!\n");

                }
            }

            if (modelSessionBeanRemote.modelInUse(m.getId())) {

                System.out.println("Unfortunately, you cannot delete the model as it has already been used currently");

                m.setEnabled(false);

                modelSessionBeanRemote.updateModel(m);

                System.out.println("System have disable make & model with Id = " + m.getId());

            } else {

                doDeleteModel(m);
            }

            break;
        }
    }

    private void doDeleteModel(Model model) {

        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** Delete Rental Rate ***\n");
        System.out.printf("Confirm Delete Make %s & Model %s (Make and Model Id: %d) (Enter 'Y' to Delete)> ", model.getMake(), model.getModel(), model.getId());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {

            modelSessionBeanRemote.deleteModel(model.getId());
            System.out.println("Make and Model deleted successfully!\n");

        } else {
            System.out.println("Make and Model NOT deleted!\n");
        }

    }

    private void createNewCar() {

        Car newCar = new Car();
        Scanner scanner = new Scanner(System.in);
        Outlet outlet;
        Model model;
        Integer integerInput;

        List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlet();
        List<Model> models = modelSessionBeanRemote.getModels();

        System.out.println("\nList of Outlets: ");

        for (int i = 0; i < outlets.size(); i++) {

            System.out.println((i + 1) + ". " + outlets.get(i).getOutletId());
        }

        while (true) {

            System.out.print("\nSelect which Outlet is the Car in > ");
            integerInput = scanner.nextInt();

            if (integerInput >= 1 && integerInput <= outlets.size()) {

                outlet = outlets.get(integerInput - 1);
                break;

            } else {

                System.out.println("Invalid option, please try again!\n");

            }
        }

        for (int i = 0; i < models.size(); i++) {

            System.out.println((i + 1) + ". Make : " + models.get(i).getMake() + "  Model : " + models.get(i).getModel());
        }

        while (true) {

            System.out.print("\nSelect Make and Model of the car > ");
            integerInput = scanner.nextInt();

            if (integerInput >= 1 && integerInput <= models.size()) {

                model = models.get(integerInput - 1);
                break;

            } else {

                System.out.println("Invalid option, please try again!\n");

            }
        }
        
        scanner.nextLine();
        
        System.out.print("\nInput License Plate Number >");
        String licensePlateNumber = scanner.nextLine();
        newCar.setLicensePlateNumber(licensePlateNumber);
        
        System.out.print("\nInput colour >");
        String colour = scanner.nextLine();
        newCar.setColour(colour);
        
        newCar.setStatus(CarStatusEnum.AVAILABLE);
        newCar.setLocation(LocationEnum.OUTLET);
        newCar.setRentalEndDate(null);
        newCar.setRentalStartDate(null);
        
        long newCarId = carSessionBeanRemote.createCar(newCar, model.getId(), outlet.getOutletId());
        System.out.println(String.format("\nYou have created Car with the id of %d", newCarId));

    }

    private void viewAllCar() {
    }

    private void viewTransitDriverDispatchRecords() {
    }

    private void assignTransitDriver() {
    }

    private void updateTransitAsCompleted() {
    }
}
