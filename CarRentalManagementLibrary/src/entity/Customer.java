/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author jarrentoh
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long CustomerId;

    @Column(nullable = false, length = 32, unique = true)
    private String MobilePhoneNo;

    @Column(nullable = false, length = 32, unique = true)
    private String email;

    @Column(nullable = false, length = 32, unique = true)
    private String passportNo;

    @Column(nullable = false, length = 32)
    private String password;

    @Column(nullable = false, length = 32)
    private String CreditCardDetails;

    @Column(nullable = false)
    private boolean paid;

    @OneToOne
    private Car car;

    //Changed the cardinality of the assciation
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet outlet;

    public Customer() {
    }

    public Customer(String MobilePhoneNo, String email, String passportNo, String password, String CreditCardDetails, boolean paid, Car car, Outlet outlet) {
        this.MobilePhoneNo = MobilePhoneNo;
        this.email = email;
        this.passportNo = passportNo;
        this.password = password;
        this.CreditCardDetails = CreditCardDetails;
        this.paid = paid;
        this.car = car;
        this.outlet = outlet;
    }
    
    public Long getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(Long CustomerId) {
        this.CustomerId = CustomerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (CustomerId != null ? CustomerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the CustomerId fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.CustomerId == null && other.CustomerId != null) || (this.CustomerId != null && !this.CustomerId.equals(other.CustomerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + CustomerId + " ]";
    }

    /**
     * @return the MobilePhoneNo
     */
    public String getMobilePhoneNo() {
        return MobilePhoneNo;
    }

    /**
     * @param MobilePhoneNo the MobilePhoneNo to set
     */
    public void setMobilePhoneNo(String MobilePhoneNo) {
        this.MobilePhoneNo = MobilePhoneNo;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the passportNo
     */
    public String getPassportNo() {
        return passportNo;
    }

    /**
     * @param passportNo the passportNo to set
     */
    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the CreditCardDetails
     */
    public String getCreditCardDetails() {
        return CreditCardDetails;
    }

    /**
     * @param CreditCardDetails the CreditCardDetails to set
     */
    public void setCreditCardDetails(String CreditCardDetails) {
        this.CreditCardDetails = CreditCardDetails;
    }

    /**
     * @return the paid
     */
    public boolean isPaid() {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(boolean paid) {
        this.paid = paid;
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

}
