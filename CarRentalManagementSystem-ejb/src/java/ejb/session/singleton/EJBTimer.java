/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.Car;
import entity.TransitDriverRecord;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Timeout;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jarrentoh
 */
@Singleton
@LocalBean
public class EJBTimer {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    @Timeout
    @Schedule(hour = "*", minute = "*", second = "*/1")
    public void AllocateCarsToCurrentDayReservations() {
        
        System.out.println("********** AllocateCarsToCurrentDayReservations()");
        
//        Query query = em.createQuery("SELECT r FROM Reserved r WHERE r.car")
//        
    }

    @Schedule(hour = "*", minute = "*", second = "*/1")
    public void GenerateTransitDriverDispatchRecordsForCurrentDayReservation() {

        
        System.out.println("********** GenerateTransitDriverDispatchRecordsForCurrentDayReservation()");
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.reserved.pickUpOutlet <> c.outlet.address");;

        List<Car> cars = query.getResultList();

        for (Car car : cars) {
            
            if(car.getRentalStartDate() == null) {
                
                cars.remove(car);

            } else if (car.getRentalStartDate() != null &&  !car.getRentalStartDate().toLocalDate().equals(LocalDate.now())) {

                cars.remove(car);

            }

        }

        for (Car car : cars) {
            
            TransitDriverRecord transitDriverRecord = new TransitDriverRecord();
            
            transitDriverRecord.setCar(car);
            
            em.persist(transitDriverRecord);
        }

    }

}
