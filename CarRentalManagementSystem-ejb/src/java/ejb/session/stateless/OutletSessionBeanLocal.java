/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.time.LocalTime;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wjahoward
 */
@Local
public interface OutletSessionBeanLocal {

    Long createNewOutlet(Outlet newOutlet);

    Outlet getOutlet(String outletName);

    List<Outlet> getOutletWithPickAndReturnTime(LocalTime pickupTime, LocalTime returnTime, String returnOutlet);

    List<Outlet> retrieveAllOutlet();

    Outlet getOutletForPickup(LocalTime pickupTime, String outlet);
    
    Outlet getOutletForReturn(LocalTime returnTime, String outletName);
    
    List<Outlet> getOutletsAvailableForReturn(LocalTime returnTime);
}
