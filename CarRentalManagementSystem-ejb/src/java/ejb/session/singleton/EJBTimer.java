/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.Car;
import entity.Reserved;
import entity.TransitDriverRecord;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Timeout;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CarStatusEnum;
import util.enumeration.LocationEnum;

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
    @Schedule(dayOfWeek= "*")
    public void AllocateCarsToCurrentDayReservations() {

        System.out.println("********** AllocateCarsToCurrentDayReservations()");

        Query query = em.createQuery("SELECT r FROM Reserved r");
        List<Reserved> reserveds = query.getResultList();

        for (Reserved reserved : reserveds) {

            if (!reserved.getRentalStartDate().toLocalDate().equals(LocalDate.now())) {

                reserveds.remove(reserved);
            }

        }

        for (Reserved reserved : reserveds) {

            if (reserved.getCategory() != null) {

                Query qInOutlet = em.createQuery("SELECT c FROM Car c WHERE c.outlet.address =:pickUpOutlet AND c.model.category =:category AND c.location =:location AND c.status =:status");
                qInOutlet.setParameter("pickUpOutlet", reserved.getPickUpOutlet());
                qInOutlet.setParameter("category", reserved.getCategory());
                qInOutlet.setParameter("location", LocationEnum.OUTLET);
                qInOutlet.setParameter("status", CarStatusEnum.AVAILABLE);

                Car carInOutlet = (Car) qInOutlet.getSingleResult();

                if (carInOutlet != null) {

                    reserved.setCar(carInOutlet);
                    carInOutlet.setReserved(reserved);
                    carInOutlet.setLocation(LocationEnum.SPECIFIC_CUSTOMER);
                    carInOutlet.setStatus(CarStatusEnum.UNAVAILABLE);

                } else {

                    Query qInOutletUnavailable = em.createQuery("SELECT c FROM Car c WHERE c.outlet.address =:pickUpOutlet AND c.model.category =:category AND c.location =:location AND c.status =:status");
                    qInOutletUnavailable.setParameter("pickUpOutlet", reserved.getPickUpOutlet());
                    qInOutletUnavailable.setParameter("category", reserved.getCategory());
                    qInOutletUnavailable.setParameter("location", LocationEnum.SPECIFIC_CUSTOMER);
                    qInOutletUnavailable.setParameter("status", CarStatusEnum.UNAVAILABLE);

                    List<Car> carsInOutletUnavailable = qInOutletUnavailable.getResultList();
                    
                    for (Car c : carsInOutletUnavailable) {

                        if (c.getReserved().getRentalEndDate().isBefore(reserved.getRentalStartDate())) {

                            reserved.setCar(c);
                            c.setReserved(reserved);
                            break;
                        }
                    }

                }

                if (reserved.getCar() == null) {

                    Query qOtherOutlet = em.createQuery("SELECT c FROM Car c WHERE c.outlet.address <>:pickUpOutlet AND c.model.category =:category AND c.location =:location AND c.status =:status");
                    qOtherOutlet.setParameter("pickUpOutlet", reserved.getPickUpOutlet());
                    qOtherOutlet.setParameter("category", reserved.getCategory());
                    qOtherOutlet.setParameter("location", LocationEnum.OUTLET);
                    qOtherOutlet.setParameter("status", CarStatusEnum.AVAILABLE);

                    Car carInOtherOutlet = (Car) qOtherOutlet.getSingleResult();
                    reserved.setCar(carInOtherOutlet);
                    carInOtherOutlet.setReserved(reserved);
                    carInOtherOutlet.setLocation(LocationEnum.SPECIFIC_CUSTOMER);
                    carInOtherOutlet.setStatus(CarStatusEnum.UNAVAILABLE);
                }

            } else if (reserved.getModel() != null) {

                Query qInOutlet = em.createQuery("SELECT c FROM Car c WHERE c.outlet.address =:pickUpOutlet AND c.model =:model AND c.location =:location AND c.status =:status");
                qInOutlet.setParameter("pickUpOutlet", reserved.getPickUpOutlet());
                qInOutlet.setParameter("mode;", reserved.getModel());
                qInOutlet.setParameter("location", LocationEnum.OUTLET);
                qInOutlet.setParameter("status", CarStatusEnum.AVAILABLE);

                Car carInOutlet = (Car) qInOutlet.getSingleResult();

                if (carInOutlet != null) {

                    reserved.setCar(carInOutlet);
                    carInOutlet.setReserved(reserved);
                    carInOutlet.setLocation(LocationEnum.SPECIFIC_CUSTOMER);
                    carInOutlet.setStatus(CarStatusEnum.UNAVAILABLE);

                } else {

                    Query qInOutletUnavailable = em.createQuery("SELECT c FROM Car c WHERE c.outlet.address =:pickUpOutlet AND c.model =:model AND c.location =:location AND c.status =:status");
                    qInOutletUnavailable.setParameter("pickUpOutlet", reserved.getPickUpOutlet());
                    qInOutletUnavailable.setParameter("model", reserved.getModel());
                    qInOutletUnavailable.setParameter("location", LocationEnum.SPECIFIC_CUSTOMER);
                    qInOutletUnavailable.setParameter("status", CarStatusEnum.UNAVAILABLE);

                    List<Car> carsInOutletUnavailable = qInOutletUnavailable.getResultList();
                    for (Car c : carsInOutletUnavailable) {

                        if (c.getReserved().getRentalEndDate().isBefore(reserved.getRentalStartDate())) {

                            reserved.setCar(c);
                            c.setReserved(reserved);
                            break;
                        }
                    }

                }

                if (reserved.getCar() == null) {

                    Query qOtherOutlet = em.createQuery("SELECT c FROM Car c WHERE c.outlet.address <>:pickUpOutlet AND c.model =:model AND c.location =:location AND c.status =:status");
                    qOtherOutlet.setParameter("pickUpOutlet", reserved.getPickUpOutlet());
                    qOtherOutlet.setParameter("model", reserved.getModel());
                    qOtherOutlet.setParameter("location", LocationEnum.OUTLET);
                    qOtherOutlet.setParameter("status", CarStatusEnum.AVAILABLE);

                    Car carInOtherOutlet = (Car) qOtherOutlet.getSingleResult();
                    reserved.setCar(carInOtherOutlet);
                    carInOtherOutlet.setReserved(reserved);
                    carInOtherOutlet.setLocation(LocationEnum.SPECIFIC_CUSTOMER);
                    carInOtherOutlet.setStatus(CarStatusEnum.UNAVAILABLE);
                }

            }

        }

    }

    @Schedule(dayOfWeek= "*")
    public void GenerateTransitDriverDispatchRecordsForCurrentDayReservation() {

        System.out.println("********** GenerateTransitDriverDispatchRecordsForCurrentDayReservation()");
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.reserved.pickUpOutlet <> c.outlet.address");;

        List<Car> cars = query.getResultList();

        for (Car car : cars) {

            if (car.getReserved().getRentalStartDate() == null) {

                cars.remove(car);

            } else if (car.getReserved().getRentalStartDate() != null && !car.getReserved().getRentalStartDate().toLocalDate().equals(LocalDate.now())) {

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
