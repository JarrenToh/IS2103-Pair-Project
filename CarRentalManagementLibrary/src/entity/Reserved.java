/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import util.enumeration.PaidStatus;

/**
 *
 * @author jarrentoh
 */
@Entity
public class Reserved implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservedId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaidStatus paid;

    @Column(nullable = false)
    private BigDecimal totalCost;

    @Column(nullable = false)
    private String pickUpOutlet;

    @Column(nullable = false)
    private String returnOutlet;

    @Column(nullable = true)
    private LocalDateTime rentalStartDate;

    @Column(nullable = true)
    private LocalDateTime rentalEndDate;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;

    @OneToOne(optional = true)
    private Car car;

    @OneToOne(mappedBy = "reserved", optional = true)
    private Model model;

    @OneToOne(mappedBy = "reserved", optional = true)
    private Category category;

    public Reserved() {
        this.paid = PaidStatus.UNPAID;
    }

    public Long getReservedId() {
        return reservedId;
    }

    public void setReservedId(Long reservedId) {
        this.reservedId = reservedId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservedId != null ? reservedId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservedId fields are not set
        if (!(object instanceof Reserved)) {
            return false;
        }
        Reserved other = (Reserved) object;
        if ((this.reservedId == null && other.reservedId != null) || (this.reservedId != null && !this.reservedId.equals(other.reservedId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reserved[ id=" + reservedId + " ]";
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
     * @return the totalCost
     */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * @return the pickUpOutlet
     */
    public String getPickUpOutlet() {
        return pickUpOutlet;
    }

    /**
     * @param pickUpOutlet the pickUpOutlet to set
     */
    public void setPickUpOutlet(String pickUpOutlet) {
        this.pickUpOutlet = pickUpOutlet;
    }

    /**
     * @return the returnOutlet
     */
    public String getReturnOutlet() {
        return returnOutlet;
    }

    /**
     * @param returnOutlet the returnOutlet to set
     */
    public void setReturnOutlet(String returnOutlet) {
        this.returnOutlet = returnOutlet;
    }

    /**
     * @return the paid
     */
    public PaidStatus getPaid() {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(PaidStatus paid) {
        this.paid = paid;
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
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the rentalStartDate
     */
    public LocalDateTime getRentalStartDate() {
        return rentalStartDate;
    }

    /**
     * @param rentalStartDate the rentalStartDate to set
     */
    public void setRentalStartDate(LocalDateTime rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    /**
     * @return the rentalEndDate
     */
    public LocalDateTime getRentalEndDate() {
        return rentalEndDate;
    }

    /**
     * @param rentalEndDate the rentalEndDate to set
     */
    public void setRentalEndDate(LocalDateTime rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

}
