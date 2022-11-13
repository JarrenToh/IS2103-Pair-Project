/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Outlet;
import entity.Reserved;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CarStatusEnum;
import util.enumeration.LocationEnum;
import util.enumeration.PaidStatus;

/**
 *
 * @author jarrentoh
 */
@Stateless
public class ReservedSessionBean implements ReservedSessionBeanRemote, ReservedSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public ReservedSessionBean() {
    }

    //Allocate current reservation to car and customer
    @Override
    public long allocateReservedToCar(Reserved reserved, long carId) {

        Reserved r = em.find(Reserved.class, reserved.getReservedId());
        
        Car car = em.find(Car.class, carId);

        //associate Car and reserved
        r.setCar(car);
        car.setReserved(r);

        return reserved.getReservedId();
    }

    @Override
    public Reserved findReservation(long carId, long customerId) {

        Query query = em.createQuery("SELECT r FROM Reserved r WHERE r.car.carId = :carId AND r.customer.CustomerId = :customerId");
        query.setParameter("carId", carId);
        query.setParameter("customerId", customerId);

        return (Reserved) query.getSingleResult();
    }

    @Override
    public Long pickUpCar(long carId, long customerId) {

        Reserved reserved = findReservation(carId, customerId);

        if (reserved.getPaid() == PaidStatus.UNPAID) {

            reserved.setPaid(PaidStatus.PAID);
        }

        Car car = em.find(Car.class, reserved.getCar().getCarId());
        car.setLocation(LocationEnum.SPECIFIC_CUSTOMER);
        car.setStatus(CarStatusEnum.UNAVAILABLE);

        return reserved == null ? null : reserved.getReservedId();
    }

    @Override
    public Long returnCar(long carId, long customerId, long outletId) {

        Reserved reserved = findReservation(carId, customerId);

        Car car = em.find(Car.class, reserved.getCar().getCarId());
        Outlet previousOutlet = em.find(Outlet.class, car.getOutlet().getOutletId());
        Outlet returnOutlet = em.find(Outlet.class, outletId);

        //remove car from previous Outlet
        previousOutlet.getCars().remove(car);

        //add car to return Outlet
        returnOutlet.getCars().add(car);
        car.setOutlet(returnOutlet);

        car.setLocation(LocationEnum.OUTLET);
        car.setStatus(CarStatusEnum.AVAILABLE);

        return reserved == null ? null : reserved.getReservedId();

    }

    @Override
    public Reserved viewSpecificReservation(long reservedId) {
        
        Reserved reserved = em.find(Reserved.class, reservedId);
        
        return reserved;
    }

    @Override
    public List<Reserved> viewAllReservationOfCustomer(long customerId) {
        
        Query query = em.createQuery("SELECT r FROM Reserved r WHERE r.customer.CustomerId = :customerId");
        query.setParameter("customerId", customerId);
        
        return query.getResultList();
    }

    @Override
    public BigDecimal CancelReservation(long reservationId) {
        
        Reserved reserved = em.find(Reserved.class, reservationId);
        BigDecimal penaltyFactor = new BigDecimal("0.00");
        BigDecimal refundAmount = new BigDecimal("0.00");
        
        int daysBeforePickUp = reserved.getRentalStartDate().compareTo(LocalDateTime.now());
        
        if(daysBeforePickUp < 14 && daysBeforePickUp >= 7) {
        
            penaltyFactor = new BigDecimal("0.20");
            
        } else if (daysBeforePickUp < 7 && daysBeforePickUp >= 3) {
        
            penaltyFactor = new BigDecimal("0.50");
            
        } else if (daysBeforePickUp < 3) {
            
            penaltyFactor = new BigDecimal("0.70");
        
        } 
        
        if(reserved.getPaid() == PaidStatus.PAID) {
            
            //If refundAmount is positive we refund customer
            refundAmount = reserved.getTotalCost().multiply(BigDecimal.ONE.subtract(penaltyFactor));
        
        } else {
            
            //If refundAmount is negative we need to charge it to customer
            refundAmount = reserved.getTotalCost().multiply(penaltyFactor).negate();
            
        }
        
        reserved.setPaid(PaidStatus.REFUND);
        
        
        return refundAmount;
    }
    
    
    
}
