/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author jarrentoh
 */
@Entity
public class Outlet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    private Date openingTime;
    
    @Column(nullable = false)
    private Date closingTime;
    
    @OneToMany(mappedBy="outlet")
    private List<Car> cars = new ArrayList<>();
    
    @OneToMany(mappedBy="outlet")
    private List<Employee> employees = new ArrayList<>();
    
    //Not very sure about this
    @ManyToMany(mappedBy = "outlets")
    private List<Customer> customers;

    public Outlet() {
    }

    public Outlet(String address, Date openingTime, Date closingTime) {
        this.address = address;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }
    
    
    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof Outlet)) {
            return false;
        }
        Outlet other = (Outlet) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Outlet[ id=" + outletId + " ]";
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the openingTime
     */
    public Date getOpeningTime() {
        return openingTime;
    }

    /**
     * @param openingTime the openingTime to set
     */
    public void setOpeningTime(Date openingTime) {
        this.openingTime = openingTime;
    }

    /**
     * @return the closingTime
     */
    public Date getClosingTime() {
        return closingTime;
    }

    /**
     * @param closingTime the closingTime to set
     */
    public void setClosingTime(Date closingTime) {
        this.closingTime = closingTime;
    }

    /**
     * @return the cars
     */
    public List<Car> getCars() {
        return cars;
    }

    /**
     * @param cars the cars to set
     */
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    /**
     * @return the employees
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * @param employees the employees to set
     */
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    /**
     * @return the customers
     */
    public List<Customer> getCustomers() {
        return customers;
    }

    /**
     * @param customers the customers to set
     */
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    
}
