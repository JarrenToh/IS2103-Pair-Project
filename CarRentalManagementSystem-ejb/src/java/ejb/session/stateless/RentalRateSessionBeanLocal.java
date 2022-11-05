/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.RentalRate;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wjahoward
 */
@Local
public interface RentalRateSessionBeanLocal {
    long createRentalRate(RentalRate rr);
    List<RentalRate> getRentalRatesWithCategories();
    List<RentalRate> getRentalRates();
    RentalRate getSpecificRental(long rentalRateId);
    long updateRentalRate(RentalRate rr);
    void deleteRentalRate(RentalRate rr);
}
