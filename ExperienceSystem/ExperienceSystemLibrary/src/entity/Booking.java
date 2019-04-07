/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumerated.StatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Asus
 */
@Entity
@XmlRootElement
public class Booking implements Serializable {

  
    @NotNull
    @ManyToOne
    private User guest;

//    @NotNull
    private boolean userEvaluated;
//    @NotNull
    private boolean hostEvaluated;
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingDate;
    @NotNull
    @Min(1)
    @Max(10)
    private Integer numberOfPeople;
    @NotNull
    private BigDecimal totalPrice;
    @NotNull
    private StatusEnum status;
    @ManyToOne
    private ExperienceDate experienceDate;
    @OneToOne(mappedBy = "booking")
    private Evaluation evaluationByGuest;
    @OneToOne(mappedBy = "booking")
    private Evaluation evaluationByHost;
    @OneToOne
    private ExperienceDateCancellationReport cancellationReport;

    public Booking() {
        this.status = StatusEnum.ACTIVE;
    }

    
    public ExperienceDateCancellationReport getCancellationReport() {
        return cancellationReport;
    }

    public void setCancellationReport(ExperienceDateCancellationReport cancellationReport) {
        this.cancellationReport = cancellationReport;
    }

    public ExperienceDate getExperienceDate() {
        return experienceDate;
    }

    public void setExperienceDate(ExperienceDate experienceDate) {
        this.experienceDate = experienceDate;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookingId != null ? bookingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the bookingId fields are not set
        if (!(object instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) object;
        if ((this.bookingId == null && other.bookingId != null) || (this.bookingId != null && !this.bookingId.equals(other.bookingId))) {
            return false;
        }
        return true;
    }

    public Evaluation getEvaluationByGuest() {
        return evaluationByGuest;
    }

    public void setEvaluationByGuest(Evaluation evaluationByGuest) {
        this.evaluationByGuest = evaluationByGuest;
    }

    public Evaluation getEvaluationByHost() {
        return evaluationByHost;
    }

    public void setEvaluationByHost(Evaluation evaluationByHost) {
        this.evaluationByHost = evaluationByHost;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "entity.Booking[ id=" + bookingId + " ]";
    }

    public User getGuest() {
        return guest;
    }

    public void setGuest(User guest) {
        this.guest = guest;
    }

    public boolean isUserEvaluated() {
        return userEvaluated;
    }

    public void setUserEvaluated(boolean userEvaluated) {
        this.userEvaluated = userEvaluated;
    }

    public boolean isHostEvaluated() {
        return hostEvaluated;
    }

    public void setHostEvaluated(boolean hostEvaluated) {
        this.hostEvaluated = hostEvaluated;
    }

   
    
}
