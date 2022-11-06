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
            m.setMakeAndModelName(name);
                        
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
        System.out.println("\nCar Category ----- Model");
        for (int i = 0; i < models.size(); i++) {
            Model m = models.get(i);
            System.out.println(m.getCategory().getCategoryName() + " ----- " + m.getMakeAndModelName());
        }
    }
    
    private void updateModel()
    {
        
    }
    
    private void deleteModel()
    {
        
    }
    

}
