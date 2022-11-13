/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jarrentoh
 */
@Entity
public class TransitDriverRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitDriverId;
    
    @Column(nullable = false)
    @NotNull
    private boolean completed;
    
    @OneToOne(optional = false)
    private Employee employee;
    
    @OneToOne(optional = false)
    private Car car;

    public TransitDriverRecord() {
        completed = false;
    }

    public TransitDriverRecord(Employee employee, Car car) {
        this();
        this.employee = employee;
        this.car = car;
    }

    public Long getTransitDriverId() {
        return transitDriverId;
    }

    public void setTransitDriverId(Long transitDriverId) {
        this.transitDriverId = transitDriverId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitDriverId != null ? transitDriverId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitDriverId fields are not set
        if (!(object instanceof TransitDriverRecord)) {
            return false;
        }
        TransitDriverRecord other = (TransitDriverRecord) object;
        if ((this.transitDriverId == null && other.transitDriverId != null) || (this.transitDriverId != null && !this.transitDriverId.equals(other.transitDriverId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverRecord[ id=" + transitDriverId + " ]";
    }

    /**
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * @return the car
     */
    public Car getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * @return the completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * @param completed the completed to set
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
}
