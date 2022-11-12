/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Customer;
import entity.Outlet;
import entity.Reserved;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CarStatusEnum;
import util.enumeration.LocationEnum;

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

    @Override
    public long createNewReservation(Reserved reserved, long carId, long customerId) {

        em.persist(reserved);

        Car car = em.find(Car.class, carId);
        Customer customer = em.find(Customer.class, customerId);

        //associate Car and reserved
        reserved.setCar(car);
        car.getReserveds().add(reserved);

        //associate customer and reserved
        reserved.setCustomer(customer);
        customer.getReserveds().add(reserved);

        em.flush();

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

        if (!reserved.isPaid()) {

            reserved.setPaid(true);
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

}
