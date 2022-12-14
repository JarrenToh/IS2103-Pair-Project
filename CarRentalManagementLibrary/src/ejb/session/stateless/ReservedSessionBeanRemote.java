/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import entity.Reserved;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author jarrentoh
 */
@Remote
public interface ReservedSessionBeanRemote {

    long allocateReservedToCar(Reserved reserved, long carId);

    Reserved findReservation(long carId, long customerId);

    Long pickUpCar(long carId, long customerId);

    Long returnCar(long carId, long customerId, long outletId);

    Reserved viewSpecificReservation(long reservedId);

    List<Reserved> viewAllReservationOfCustomer(long customerId);

    BigDecimal CancelReservation(long reservationId);
    
    List<Reserved> getReservedRecords();
    
    List<Reserved> getNumberOfMReservedByOutlet(Model m, String pickupOutlet);
    
    long createReservation(Reserved reserved, long customerId);
    
}
