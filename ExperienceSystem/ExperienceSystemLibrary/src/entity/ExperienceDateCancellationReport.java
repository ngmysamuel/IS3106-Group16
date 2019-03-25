/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Asus
 */
@Entity
public class ExperienceDateCancellationReport implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cancellationReportId;
    @OneToOne(mappedBy = "cancellationReport")
    private Booking booking;
    @NotNull
    private String cancellationReason;
    @OneToOne
    private ExperienceDate experienceDate;

    public ExperienceDateCancellationReport() {
    }
    

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public ExperienceDate getExperienceDate() {
        return experienceDate;
    }

    public void setExperienceDate(ExperienceDate experienceDate) {
        this.experienceDate = experienceDate;
    }

    public Long getCancellationReportId() {
        return cancellationReportId;
    }

    public void setCancellationReportId(Long cancellationReportId) {
        this.cancellationReportId = cancellationReportId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cancellationReportId != null ? cancellationReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the cancellationReportId fields are not set
        if (!(object instanceof ExperienceDateCancellationReport)) {
            return false;
        }
        ExperienceDateCancellationReport other = (ExperienceDateCancellationReport) object;
        if ((this.cancellationReportId == null && other.cancellationReportId != null) || (this.cancellationReportId != null && !this.cancellationReportId.equals(other.cancellationReportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ExperienceDateCancellationReport[ id=" + cancellationReportId + " ]";
    }
    
}
