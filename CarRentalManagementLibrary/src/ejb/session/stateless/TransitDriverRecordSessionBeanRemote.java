/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverRecord;
import javax.ejb.Remote;

/**
 *
 * @author wjahoward
 */
@Remote
public interface TransitDriverRecordSessionBeanRemote {
    void createNewTransitDriverRecord(TransitDriverRecord newTransitDriverRecord);
}
