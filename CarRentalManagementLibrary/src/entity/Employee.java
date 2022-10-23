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
import util.enumeration.DispatchStatusEnum;
import util.enumeration.EmployeeAccessRightEnum;

/**
 *
 * @author jarrentoh
 */
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeAccessRightEnum userRole;

    @Column(nullable = false, unique = true, length = 32)
    private String userName;

    @Column(nullable = false, length = 32)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DispatchStatusEnum dispatchStatus;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet outlet;
    
    @OneToOne(mappedBy = "employee")
    private TransitDriverRecord transitDriverRecord;
    
    
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Employee[ id=" + employeeId + " ]";
    }

    /**
     * @return the userRole
     */
    public EmployeeAccessRightEnum getUserRole() {
        return userRole;
    }

    /**
     * @param userRole the userRole to set
     */
    public void setUserRole(EmployeeAccessRightEnum userRole) {
        this.userRole = userRole;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
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
     * @return the dispatchStatus
     */
    public DispatchStatusEnum getDispatchStatus() {
        return dispatchStatus;
    }

    /**
     * @param dispatchStatus the dispatchStatus to set
     */
    public void setDispatchStatus(DispatchStatusEnum dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
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

}
