/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.RentalRate;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author wjahoward
 */
@Remote
public interface RentalRateSessionBeanRemote {
    long createRentalRate(RentalRate rr, Category c);
    List<RentalRate> getRentalRatesWithCategories();
    List<RentalRate> getRentalRates();
    RentalRate getSpecificRental(long rentalRateId);
    List<RentalRate> getRentalRatesByCategoryIdBetweenPickupAndReturn(long categoryId, LocalDateTime pickupDateTime, LocalDateTime returnDateTime);
    long updateRentalRate(RentalRate rr);
    void deleteRentalRate(long rentalRateId);
}
