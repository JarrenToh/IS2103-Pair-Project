/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverRecord;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wjahoward
 */
@Local
public interface TransitDriverRecordSessionBeanLocal {
    void createNewTransitDriverRecord(TransitDriverRecord newTransitDriverRecord, long carId);

    List<TransitDriverRecord> getTransitDriverRecord();

    void assignTransitDriver(long transitDriverId, long employeeId);

    void updateTransitDriverRecordAsCompleted(long transitDriverId);

    TransitDriverRecord getSpecificTransitDriverRecord(long transitDriverId);

    List<TransitDriverRecord> getTransitDriverRecordForCurrentDay(long outletId);
}
