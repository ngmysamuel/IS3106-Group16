/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Asus
 */
@Entity
@XmlRootElement
public class ExperienceDatePaymentReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentReportId;
    @NotNull
    private Date paymentDate;
    @NotNull
    private BigDecimal paymentAmount;
    @OneToOne(mappedBy = "experienceDatePaymentReport")
    private ExperienceDate experienceDate;

    public ExperienceDatePaymentReport() {
    }

    
    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public ExperienceDate getExperienceDate() {
        return experienceDate;
    }

    public void setExperienceDate(ExperienceDate experienceDate) {
        this.experienceDate = experienceDate;
    }

    public Long getPaymentReportId() {
        return paymentReportId;
    }

    public void setPaymentReportId(Long paymentReportId) {
        this.paymentReportId = paymentReportId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paymentReportId != null ? paymentReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the paymentReportId fields are not set
        if (!(object instanceof ExperienceDatePaymentReport)) {
            return false;
        }
        ExperienceDatePaymentReport other = (ExperienceDatePaymentReport) object;
        if ((this.paymentReportId == null && other.paymentReportId != null) || (this.paymentReportId != null && !this.paymentReportId.equals(other.paymentReportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ExperienceDatePaymentReport[ id=" + paymentReportId + " ]";
    }
    
}
