/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Asus
 */
@Entity
public class ExperienceDate implements Serializable {

    @OneToOne(mappedBy = "experienceDate")
    private ExperienceDateCancellationReport experienceDateCancellationReport;
    @OneToMany(mappedBy = "experienceDate")
    private List<Booking> bookings;
    @ManyToOne
    private Experience experience;
    @OneToOne
    private ExperienceDatePaymentReport experienceDatePaymentReport;
    
    
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long experienceDateId;
        @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private Integer capacity;
    @NotNull
    private Integer spotsAvailable;
    @NotNull
    private BigDecimal price;
    @NotNull
    private boolean active;
    
    
    
    
    
    
    
    
    
    public ExperienceDateCancellationReport getExperienceDateCancellationReport() {
        return experienceDateCancellationReport;
    }

    public void setExperienceDateCancellationReport(ExperienceDateCancellationReport experienceDateCancellationReport) {
        this.experienceDateCancellationReport = experienceDateCancellationReport;
    }

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
    
}
