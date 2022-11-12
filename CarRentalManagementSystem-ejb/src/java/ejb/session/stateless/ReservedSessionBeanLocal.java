/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reserved;
import javax.ejb.Local;

/**
 *
 * @author jarrentoh
 */
@Local
public interface ReservedSessionBeanLocal {

    long createNewReservation(Reserved reserved, long carId, long customerId);

    Reserved findReservation(long carId, long customerId);

    Long pickUpCar(long carId, long customerId);

    Long returnCar(long carId, long customerId, long outletId);


}
