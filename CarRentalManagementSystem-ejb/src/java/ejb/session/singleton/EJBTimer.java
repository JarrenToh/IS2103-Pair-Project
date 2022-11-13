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
import javax.ejb.Schedule;
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

    public void AllocateCarsToCurrentDayReservations() {
        
        
        
    }

    @Schedule(dayOfWeek = "*")
    public void GenerateTransitDriverDispatchRecordsForCurrentDayReservation() {

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
