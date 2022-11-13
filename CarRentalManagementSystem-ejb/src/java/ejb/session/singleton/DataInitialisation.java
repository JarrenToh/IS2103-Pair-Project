/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import ejb.session.stateless.TEmployeeSessionBeanLocal;
import entity.Car;
import entity.Category;
import entity.Employee;
import entity.Model;
import entity.Outlet;
import entity.Partner;
import entity.RentalRate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jarrentoh
 */
@Singleton
@LocalBean
@Startup
public class DataInitialisation {

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @EJB
    private CategorySessionBeanLocal categorySessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private ModelSessionBeanLocal modelSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private TEmployeeSessionBeanLocal employeeSessionBean;

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBean;

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public DataInitialisation() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {
        if (em.find(Outlet.class, 1L) == null) {
            initializeData();
        }
    }

    private void initializeData() {
        Outlet o1 = new Outlet("Outlet A", null, null);
        Outlet o2 = new Outlet("Outlet B", null, null);
        Outlet o3 = new Outlet("Outlet C", LocalTime.parse("08:00"), LocalTime.parse("22:00"));
        outletSessionBean.createNewOutlet(o1);
        outletSessionBean.createNewOutlet(o2);
        outletSessionBean.createNewOutlet(o3);

        Employee e1 = new Employee("Employee A1", getOutlet("Outlet A"), "Sales Manager");
        Employee e2 = new Employee("Employee A2", getOutlet("Outlet A"), "Operations Manager");
        Employee e3 = new Employee("Employee A3", getOutlet("Outlet A"), "Customer Services Executive");
        Employee e4 = new Employee("Employee A4", getOutlet("Outlet A"), "Employee");
        Employee e5 = new Employee("Employee A5", getOutlet("Outlet A"), "Employee");
        Employee e6 = new Employee("Employee B1", getOutlet("Outlet B"), "Sales Manager");
        Employee e7 = new Employee("Employee B2", getOutlet("Outlet B"), "Operations Manager");
        Employee e8 = new Employee("Employee B3", getOutlet("Outlet B"), "Customer Services Executive");
        Employee e9 = new Employee("Employee C1", getOutlet("Outlet C"), "Sales Manager");
        Employee e10 = new Employee("Employee C2", getOutlet("Outlet C"), "Operations Manager");
        Employee e11 = new Employee("Employee C3", getOutlet("Outlet C"), "Customer Services Executive");
        employeeSessionBean.createNewEmployee(e1, e1.getOutlet().getOutletId());
        employeeSessionBean.createNewEmployee(e2, e2.getOutlet().getOutletId());
        employeeSessionBean.createNewEmployee(e3, e3.getOutlet().getOutletId());
        employeeSessionBean.createNewEmployee(e4, e4.getOutlet().getOutletId());
        employeeSessionBean.createNewEmployee(e5, e5.getOutlet().getOutletId());
        employeeSessionBean.createNewEmployee(e6, e6.getOutlet().getOutletId());
        employeeSessionBean.createNewEmployee(e7, e7.getOutlet().getOutletId());
        employeeSessionBean.createNewEmployee(e8, e8.getOutlet().getOutletId());
        employeeSessionBean.createNewEmployee(e9, e9.getOutlet().getOutletId());
        employeeSessionBean.createNewEmployee(e10, e10.getOutlet().getOutletId());
        employeeSessionBean.createNewEmployee(e11, e11.getOutlet().getOutletId());

        Category c1 = new Category("Standard Sedan");
        Category c2 = new Category("Family Sedan");
        Category c3 = new Category("Luxury Sedan");
        Category c4 = new Category("SUV and Minivan");
        categorySessionBean.createNewCategory(c1);
        categorySessionBean.createNewCategory(c2);
        categorySessionBean.createNewCategory(c3);
        categorySessionBean.createNewCategory(c4);

        Model m1 = new Model("Toyota", "Corolla", getCategory("Standard Sedan"));
        Model m2 = new Model("Honda", "Civic", getCategory("Standard Sedan"));
        Model m3 = new Model("Nissan", "Sunny", getCategory("Standard Sedan"));
        Model m4 = new Model("Mercedes", "E Class", getCategory("Luxury Sedan"));
        Model m5 = new Model("BMW", "5 Series", getCategory("Luxury Sedan"));
        Model m6 = new Model("Audi", "A6", getCategory("Luxury Sedan"));
        modelSessionBean.createModel(m1, m1.getCategory());
        modelSessionBean.createModel(m2, m2.getCategory());
        modelSessionBean.createModel(m3, m3.getCategory());
        modelSessionBean.createModel(m4, m4.getCategory());
        modelSessionBean.createModel(m5, m5.getCategory());
        modelSessionBean.createModel(m6, m6.getCategory());

        Car car1 = new Car("SS00A1TC", getModel("Toyota", "Corolla"), "Available", getOutlet("Outlet A"));
        Car car2 = new Car("SS00A2TC", getModel("Toyota", "Corolla"), "Available", getOutlet("Outlet A"));
        Car car3 = new Car("SS00A3TC", getModel("Toyota", "Corolla"), "Available", getOutlet("Outlet A"));
        Car car4 = new Car("SS00B1HC", getModel("Honda", "Civic"), "Available", getOutlet("Outlet B"));
        Car car5 = new Car("SS00B2HC", getModel("Honda", "Civic"), "Available", getOutlet("Outlet B"));
        Car car6 = new Car("SS00B3HC", getModel("Honda", "Civic"), "Available", getOutlet("Outlet B"));
        Car car7 = new Car("SS00C1NS", getModel("Nissan", "Sunny"), "Available", getOutlet("Outlet C"));
        Car car8 = new Car("SS00C2NS", getModel("Nissan", "Sunny"), "Available", getOutlet("Outlet C"));
        Car car9 = new Car("SS00C3NS", getModel("Nissan", "Sunny"), "Repair", getOutlet("Outlet C"));
        Car car10 = new Car("LS00A4ME", getModel("Mercedes", "E Class"), "Available", getOutlet("Outlet A"));
        Car car11 = new Car("LS00B4B5", getModel("BMW", "5 Series"), "Available", getOutlet("Outlet B"));
        Car car12 = new Car("LS00C4A6", getModel("Audi", "A6"), "Available", getOutlet("Outlet C"));
        carSessionBean.createCar(car1, car1.getModel().getId(), car1.getOutlet().getOutletId());
        carSessionBean.createCar(car2, car2.getModel().getId(), car2.getOutlet().getOutletId());
        carSessionBean.createCar(car3, car3.getModel().getId(), car3.getOutlet().getOutletId());
        carSessionBean.createCar(car4, car4.getModel().getId(), car4.getOutlet().getOutletId());
        carSessionBean.createCar(car5, car5.getModel().getId(), car5.getOutlet().getOutletId());
        carSessionBean.createCar(car6, car6.getModel().getId(), car6.getOutlet().getOutletId());
        carSessionBean.createCar(car7, car7.getModel().getId(), car7.getOutlet().getOutletId());
        carSessionBean.createCar(car8, car8.getModel().getId(), car8.getOutlet().getOutletId());
        carSessionBean.createCar(car9, car9.getModel().getId(), car9.getOutlet().getOutletId());
        carSessionBean.createCar(car10, car10.getModel().getId(), car10.getOutlet().getOutletId());
        carSessionBean.createCar(car11, car11.getModel().getId(), car11.getOutlet().getOutletId());
        carSessionBean.createCar(car12, car12.getModel().getId(), car12.getOutlet().getOutletId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        RentalRate r1 = new RentalRate("Standard Sedan - Default", "Default", getCategory("Standard Sedan"), new BigDecimal(100), null, null);
        RentalRate r2 = new RentalRate("Standard Sedan - Weekend", "Promo", getCategory("Standard Sedan"), new BigDecimal(80), LocalDateTime.parse("09/12/2022 12:00", formatter), LocalDateTime.parse("11/12/2022 00:00", formatter));
        RentalRate r3 = new RentalRate("Family Sedan - Default", "Default", getCategory("Family Sedan"), new BigDecimal(200), null, null);
        RentalRate r4 = new RentalRate("Luxury Sedan - Default", "Default", getCategory("Luxury Sedan"), new BigDecimal(300), null, null);
        RentalRate r5 = new RentalRate("Luxury Sedan - Monday", "Peak", getCategory("Luxury Sedan"), new BigDecimal(310), LocalDateTime.parse("05/12/2022 00:00", formatter), LocalDateTime.parse("05/12/2022 23:59", formatter));
        RentalRate r6 = new RentalRate("Luxury Sedan - Tuesday", "Peak", getCategory("Luxury Sedan"), new BigDecimal(320), LocalDateTime.parse("06/12/2022 00:00", formatter), LocalDateTime.parse("06/12/2022 23:59", formatter));
        RentalRate r7 = new RentalRate("Luxury Sedan - Wednesday", "Peak", getCategory("Luxury Sedan"), new BigDecimal(330), LocalDateTime.parse("07/12/2022 00:00", formatter), LocalDateTime.parse("07/12/2022 23:59", formatter));
        RentalRate r8 = new RentalRate("Luxury Sedan - Weekday", "Promo", getCategory("Luxury Sedan"), new BigDecimal(250), LocalDateTime.parse("07/12/2022 12:00", formatter), LocalDateTime.parse("08/12/2022 12:00", formatter));
        RentalRate r9 = new RentalRate("SUV and Minivan - Default", "Default", getCategory("SUV and Minivan"), new BigDecimal(400), null, null);
        rentalRateSessionBean.createRentalRate(r1, r1.getCategory());
        rentalRateSessionBean.createRentalRate(r2, r2.getCategory());
        rentalRateSessionBean.createRentalRate(r3, r3.getCategory());
        rentalRateSessionBean.createRentalRate(r4, r4.getCategory());
        rentalRateSessionBean.createRentalRate(r5, r5.getCategory());
        rentalRateSessionBean.createRentalRate(r6, r6.getCategory());
        rentalRateSessionBean.createRentalRate(r7, r7.getCategory());
        rentalRateSessionBean.createRentalRate(r8, r8.getCategory());
        rentalRateSessionBean.createRentalRate(r9, r9.getCategory());

        Partner p1 = new Partner("Holiday.com");
        partnerSessionBean.createNewPartner(p1);

    }

    private Outlet getOutlet(String outletName) {
        return this.outletSessionBean.getOutlet(outletName);
    }

    private Category getCategory(String categoryName) {
        return this.categorySessionBean.getCategory(categoryName);
    }

    private Model getModel(String make, String model) {
        return this.modelSessionBean.getModel(make, model);
    }

}
