/*
TODO: Need to include try-catch for each scanner.next()
*/
package carrentalmanagementclient;

import entity.Category;
import entity.Model;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import util.regex.GlobalRegex;

/**
 *
 * @author wjahoward
 */
public class ModelApp {
    
    private ModelModule modelModule;

    public ModelApp() {
        
    }
    
    public ModelApp(ModelModule modelModule) {
        this.modelModule = modelModule;
    }
    
    public void runModelApp() { 
       Scanner scanner = new Scanner(System.in);
       Integer response = 0;
        
       while(true)
        {
            System.out.println("\nWhat do you want to do?");
            System.out.println("1: Create Model");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model");
            System.out.println("4: Delete Model");
            System.out.println("5. Go back\n");
            response = 0;
            
            while(response < 1 || response > 5)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    createModel();
                }
                else if (response == 2) 
                {
                    viewAllModels();
                }
                else if (response == 3)
                {
                    updateModel();
                } else if (response == 4)
                {
                    deleteModel();
                }
                else if (response == 5) {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if (response == 5)
            {
                break;
            }
        }
        System.out.println("\nYou have exited out of Model page successfully\n");
    }
    
    private void createModel()
    {
        Model m = new Model();
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        List<Category> categories = modelModule.getCategorySessionBeanRemote().getCategories();
        System.out.println("\nList of Categories: ");
                    
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
        }
        
        while (true) {
            System.out.print("\nSelect which category do you want to include model i.e. 1 > ");
            response = scanner.nextInt();
            
            // TODO: include validation for once then just copy paste the tempalte for the subsequent ones
            Category c = categories.get(response - 1); // assuming if is the right input
            
            System.out.print("\nInput the (make and) model name > ");
            String name = scanner.next();
            m.setMake(name);
                        
            boolean enabled = false;
            m.setEnabled(enabled);
            
            m.setCategory(c);
            
            long id = modelModule.getModelSessionBeanRemote().createModel(m);
            System.out.println(String.format("\nYou have created model with the id of %d", id));
            // TOOD: once successful, need to include validation as well
            break;
        }
    }
    
    private void viewAllModels()
    {
        List<Model> models = modelModule.getModelSessionBeanRemote().getModelsWithCategories();
        
        if (models.isEmpty()) {
                System.out.println("No models available.");
                return;
        }
        
        System.out.println("\nCar Category ----- Model");
        for (int i = 0; i < models.size(); i++) {
            Model m = models.get(i);
            System.out.println(m.getCategory().getCategoryName() + " ----- " + m.getMake());
        }
    }
    
    private void updateModel()
    {
        Model m = new Model();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            List<Model> models = modelModule.getModelSessionBeanRemote().getModels();
            
            if (models.isEmpty()) {
                System.out.println("No models available.");
                return;
            }
            
            System.out.println("\nList of Models: ");
            for (int i = 0; i < models.size(); i++) {
                m = models.get(i);
                System.out.println((i + 1) + ". " + m.getMake());
            }   
            System.out.print("\nSelect a model to update (i.e. 1) > ");
            String model = scanner.next();
            if (model.matches(GlobalRegex.NUMBER_REGEX)) {
               int modelNumber = Integer.parseInt(model);
               m = models.get(modelNumber - 1);       
            }
         
            break; // assume if is success
        }
        
        while (true) {
            System.out.println("\nNOTE: If you don't want to update a particular field, leave it blank unless otherwise stated!!");
            List<Category> categories = modelModule.getCategorySessionBeanRemote().getCategories();
            System.out.println("\nList of Categories: ");
                    
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
            }
        
            System.out.print("\nCurrent category: \033[0;1m" + m.getCategory().getCategoryName());
            System.out.print("\nUpdate the number (corresponding to the category) you want to update to i.e. 1 > ");
            scanner.nextLine();
            String categoryNumber = scanner.nextLine(); // why scanner.nextLine() is used instead of scanner.next() - we should allow the user to input empty blank spaces if don't want to update that particular field
            
            if (categoryNumber.isEmpty()) {
                
            } else {
                if (categoryNumber.matches(GlobalRegex.NUMBER_REGEX)) {
                    int categoryNumberInt = Integer.parseInt(categoryNumber);
                    if (categoryNumberInt >= 1 && categoryNumberInt <= categories.size()) {
                        m.setCategory(categories.get(categoryNumberInt - 1));    
                    }
                }
            }

            System.out.println("\nCurrent (make and) model name: \033[0;1m" + m.getMake());
            System.out.print("Update the (make and) model > ");
            String newMakeAndModelName = scanner.nextLine();
            if (newMakeAndModelName.isEmpty()) {
                
            } else {
                m.setMake(newMakeAndModelName);    
            }
                                    
            long id = modelModule.getModelSessionBeanRemote().updateModel(m);
            System.out.println(String.format("\nYou have successfully updated model with the id of %d", id));
            break; // TODO: assuming if is success, need to update with validation checks
        }
    }
    
    private void deleteModel()
    {
        Model m = new Model();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            List<Model> models = modelModule.getModelSessionBeanRemote().getModels();
            
            if (models.isEmpty()) {
                System.out.println("No models available.");
                return;
            }
            
            System.out.println("\nList of Models: ");
            for (int i = 0; i < models.size(); i++) {
                m = models.get(i);
                System.out.println((i + 1) + ". " + m.getMake());
            }   
            
            System.out.println("\nNOTE: The deletion of model is irreversible!!");
            System.out.print("Select a model to delete (i.e. 1) > ");
            String rentalRate = scanner.next();
            if (rentalRate.matches(GlobalRegex.NUMBER_REGEX)) {
               int rentalRateNumber = Integer.parseInt(rentalRate);
               m = models.get(rentalRateNumber - 1);       
            }
            
            if (m.getEnabled()) {
                System.out.println("Unfortunately, you cannot delete the model as it has already been used currently");
                continue;
            }
            
            // prompt user
            while (true) {
                System.out.print(String.format("Are you sure you want to delete model of id %d (n for no, y for yes) > ", m.getId()));
                String response = scanner.next();
                
                if (response.equals("n")) {
                    break;
                } else if (response.equals("y")) {
                    modelModule.getModelSessionBeanRemote().deleteModel(m);
                    System.out.println("\nYou have successfully deleted the model");
                    break;
                }
            }
        }
    }
    

}
