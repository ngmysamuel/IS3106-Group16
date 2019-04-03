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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Asus
 */

// Evaluation is both for a guest to evaluate an experience and for a host to evaluate a guest
@Entity
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluationId;
    private String remark;
    @NotNull
    @Min(0)
    @Max(5)
    private BigDecimal score;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date evaluationTime;
    
    @NotNull
    @ManyToOne
    private Booking booking;
    @NotNull
    @ManyToOne
    private User userEvaluating; // associated with the booking, it indicates whether this evaluation is written by a host or a guest 
    

    public Evaluation() {
        score = new BigDecimal(0);
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Date getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(Date evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public User getUserEvaluating() {
        return userEvaluating;
    }

    public void setUserEvaluating(User userEvaluating) {
        this.userEvaluating = userEvaluating;
    }
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evaluationId != null ? evaluationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the evaluationId fields are not set
        if (!(object instanceof Evaluation)) {
            return false;
        }
        Evaluation other = (Evaluation) object;
        if ((this.evaluationId == null && other.evaluationId != null) || (this.evaluationId != null && !this.evaluationId.equals(other.evaluationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Evaluation[ id=" + evaluationId + " ]";
    }
    
}
