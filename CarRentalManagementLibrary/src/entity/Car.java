/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull
    @Size(min=8, max=8)
    private String licensePlateNumber;

    @Column(nullable = true, length = 32)
    private String colour;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private CarStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private LocationEnum location;

    @Column(nullable = false)
    private boolean enabled;


    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet outlet;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Model model;
    
    @OneToOne(mappedBy = "car")
    private TransitDriverRecord transitDriverRecord;
    
    @OneToOne(mappedBy = "car")
    private Reserved reserved;

    public Car() {
        this.enabled = true;
    }
    
    public Car(String licensePlateNumber, Model model, String status, Outlet outlet) {
        this();
        this.licensePlateNumber = licensePlateNumber;
        this.model = model;
        this.status = getCarStatus(status);
        this.outlet = outlet;
    }
    
    private CarStatusEnum getCarStatus(String status) {
        switch (status) {
            case "Available":
                return CarStatusEnum.AVAILABLE;
            case "Unavailable":
                return CarStatusEnum.UNAVAILABLE;
            case "Repair":
                return CarStatusEnum.REPAIR;
            default:
                return CarStatusEnum.TRANSIT;
        }
    }
    
    
    
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
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * @return the reserved
     */
    public Reserved getReserved() {
        return reserved;
    }

    /**
     * @param reserved the reserved to set
     */
    public void setReserved(Reserved reserved) {
        this.reserved = reserved;
    }



}