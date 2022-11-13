/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 *
 * @author jarrentoh
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CustomerId;

    @Column(nullable = false, length = 32, unique = true)
    @NotNull
    @Size(min = 8, max = 32)
    private String MobilePhoneNo;

    @Column(nullable = false, length = 32, unique = true)
    @NotNull
    @Size(min = 8, max = 32)
    private String email;

    @Column(nullable = false, length = 32, unique = true)
    @NotNull
    @Size(max = 32)
    private String passportNo;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 8, max = 32)
    private String password;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String creditCardDetails;


    @OneToMany(mappedBy = "customer")
    private List<Reserved> reserveds;

    public Customer() {
        
        this.reserveds = new ArrayList<>();
    }
    
    public Customer(String email) {
        this.email = email;
        this.creditCardDetails = "1234 5678 9012 3456";
    }

    public Customer(String MobilePhoneNo, String email, String passportNo, String password, String creditCardDetails) {
        this();
        this.MobilePhoneNo = MobilePhoneNo;
        this.email = email;
        this.passportNo = passportNo;
        this.password = password;
        this.creditCardDetails = creditCardDetails;
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
        return creditCardDetails;
    }

    /**
     * @param creditCardDetails the CreditCardDetails to set
     */
    public void setCreditCardDetails(String creditCardDetails) {
        this.creditCardDetails = creditCardDetails;
    }

    /**
     * @return the reserveds
     */
    public List<Reserved> getReserveds() {
        return reserveds;
    }

    /**
     * @param reserveds the reserveds to set
     */
    public void setReserveds(List<Reserved> reserveds) {
        this.reserveds = reserveds;
    }

}
