/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Asus
 */
@Entity
@XmlRootElement
public class ExperienceDate implements Serializable, Comparable<ExperienceDate> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experienceDateId;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @NotNull
    @Min(1)
    private Integer capacity;
    @NotNull
    @Min(0)
    private Integer spotsAvailable;
    @NotNull
    private BigDecimal price;
    // An Experience Date is active only if it has not happened (it is upcoming)
    @NotNull
    private boolean active;
    
    @OneToOne(mappedBy = "experienceDate")
    private ExperienceDateCancellationReport experienceDateCancellationReport;
    @OneToMany(mappedBy = "experienceDate")
    private List<Booking> bookings;
    @ManyToOne
    private Experience experience;
    @OneToOne
    private ExperienceDatePaymentReport experienceDatePaymentReport;

    public ExperienceDate() {
        bookings = new ArrayList();
    }
    

    public ExperienceDateCancellationReport getExperienceDateCancellationReport() {
        return experienceDateCancellationReport;
    }

    public void setExperienceDateCancellationReport(ExperienceDateCancellationReport experienceDateCancellationReport) {
        this.experienceDateCancellationReport = experienceDateCancellationReport;
    }

    @XmlTransient
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public ExperienceDatePaymentReport getExperienceDatePaymentReport() {
        return experienceDatePaymentReport;
    }

    public void setExperienceDatePaymentReport(ExperienceDatePaymentReport experienceDatePaymentReport) {
        this.experienceDatePaymentReport = experienceDatePaymentReport;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getSpotsAvailable() {
        return spotsAvailable;
    }

    public void setSpotsAvailable(Integer spotsAvailable) {
        this.spotsAvailable = spotsAvailable;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public Long getExperienceDateId() {
        return experienceDateId;
    }

    public void setExperienceDateId(Long experienceDateId) {
        this.experienceDateId = experienceDateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (experienceDateId != null ? experienceDateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the experienceDateId fields are not set
        if (!(object instanceof ExperienceDate)) {
            return false;
        }
        ExperienceDate other = (ExperienceDate) object;
        if ((this.experienceDateId == null && other.experienceDateId != null) || (this.experienceDateId != null && !this.experienceDateId.equals(other.experienceDateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ExperienceDate[ id=" + experienceDateId + " ]";
    }

    @Override
    public int compareTo(ExperienceDate o) {
        Date startDate1 = this.startDate;
        Date startDate2 = o.getStartDate();
        if (startDate1.compareTo(startDate2) < 0) {
            return -1;
        } else if (startDate2.compareTo(startDate1) > 0) {
            return 1;
        } else {
            return 0;
        }
    }
    
}
