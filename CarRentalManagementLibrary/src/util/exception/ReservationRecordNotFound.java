/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author jarrentoh
 */
public class ReservationRecordNotFound extends Exception {

    /**
     * Creates a new instance of <code>ReservationRecordNotFound</code> without
     * detail message.
     */
    public ReservationRecordNotFound() {
    }

    /**
     * Constructs an instance of <code>ReservationRecordNotFound</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ReservationRecordNotFound(String msg) {
        super(msg);
    }
}
