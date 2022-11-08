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
import util.enumeration.RentalRateType;

/**
 *
 * @author wjahoward
 */
@Entity
public class RentalRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // TODO: include unique to be true
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalRateType rentalRateType;

    @Column(nullable = false)
    private BigDecimal ratePerDay;

    @Column(nullable = true)
    private LocalDateTime startDateTime;
    
    @Column(nullable = true)
    private LocalDateTime endDateTime;


    @Column(nullable = false) // if is being rented, the enabled will be true. by default, it is false
    private Boolean enabled;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Category category;

    public RentalRate() {
    }

    public RentalRate(String name, RentalRateType rentalRateType, BigDecimal ratePerDay, LocalDateTime startDateTime, LocalDateTime endDateTime, Boolean enabled, Category category) {
        this.name = name;
        this.rentalRateType = rentalRateType;
        this.ratePerDay = ratePerDay;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.enabled = enabled;
        this.category = category;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(BigDecimal ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RentalRate)) {
            return false;
        }
        RentalRate other = (RentalRate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRate[ id=" + id + " ]";
    }

    /**
     * @return the rentalRateType
     */
    public RentalRateType getRentalRateType() {
        return rentalRateType;
    }

    /**
     * @param rentalRateType the rentalRateType to set
     */
    public void setRentalRateType(RentalRateType rentalRateType) {
        this.rentalRateType = rentalRateType;
    }

    /**
     * @return the startDateTime
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime the startDateTime to set
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return the endDateTime
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * @param endDateTime the endDateTime to set
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

}
