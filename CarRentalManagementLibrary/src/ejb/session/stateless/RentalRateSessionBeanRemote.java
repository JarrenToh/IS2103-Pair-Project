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
import util.enumeration.RentalRateType;

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
    List<RentalRate> getRentalRatesByCategoryId(long categoryId);
    long updateRentalRate(RentalRate rr);
    void deleteRentalRate(long rentalRateId);
    RentalRate getRentalRatePriceByDateTimeAndType(List<Long> rentalRatesIds, LocalDateTime tempDateTime, RentalRateType rentalRateType);
}
