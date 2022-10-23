/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import util.enumeration.CarStatusEnum;
import util.enumeration.LocationEnum;

/**
 *
 * @author jarrentoh
 */
@Entity
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    @Column(nullable = false, unique = true, length = 32)
    private String licensePlateNumber;

    @Column(nullable = false, length = 32)
    private String colour;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LocationEnum location;

    @Column(nullable = false)
    private boolean enabled;

    @Column
    private Date rentalStartDate;

    @Column
    private Date rentalEndDate;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet outlet;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Model model;
    
    @OneToOne(mappedBy = "car")
    private TransitDriverRecord transitDriverRecord;
    
    @OneToOne(mappedBy = "car")
    private Customer customer;
    
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Car[ id=" + carId + " ]";
    }

    /**
     * @return the licensePlateNumber
     */
    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    /**
     * @param licensePlateNumber the licensePlateNumber to set
     */
    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    /**
     * @return the colour
     */
    public String getColour() {
        return colour;
    }

    /**
     * @param colour the colour to set
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * @return the status
     */
    public CarStatusEnum getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(CarStatusEnum status) {
        this.status = status;
    }

    /**
     * @return the location
     */
    public LocationEnum getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(LocationEnum location) {
        this.location = location;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the rentalStartDate
     */
    public Date getRentalStartDate() {
        return rentalStartDate;
    }

    /**
     * @param rentalStartDate the rentalStartDate to set
     */
    public void setRentalStartDate(Date rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    /**
     * @return the rentalEndDate
     */
    public Date getRentalEndDate() {
        return rentalEndDate;
    }

    /**
     * @param rentalEndDate the rentalEndDate to set
     */
    public void setRentalEndDate(Date rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

    /**
     * @return the outlet
     */
    public Outlet getOutlet() {
        return outlet;
    }

    /**
     * @param outlet the outlet to set
     */
    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    /**
     * @return the transitDriverRecord
     */
    public TransitDriverRecord getTransitDriverRecord() {
        return transitDriverRecord;
    }

    /**
     * @param transitDriverRecord the transitDriverRecord to set
     */
    public void setTransitDriverRecord(TransitDriverRecord transitDriverRecord) {
        this.transitDriverRecord = transitDriverRecord;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
