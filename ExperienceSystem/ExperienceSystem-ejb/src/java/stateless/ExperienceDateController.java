/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Booking;
import entity.Experience;
import entity.ExperienceDate;
import entity.ExperienceDateCancellationReport;
import enumerated.StatusEnum;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author samue
 */
@Stateless
public class ExperienceDateController implements ExperienceDateControllerRemote, ExperienceDateControllerLocal {

    @EJB
    private ExperienceDateCancellationReportControllerLocal experienceDateCancellationReportController;

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;

    public ExperienceDate createExperienceDate(ExperienceDate expDate, Experience exp) {
        em.persist(expDate);
        em.flush();
        exp.getExperienceDates().add(expDate);
        return expDate;
    }

    public void updateExperienceDate(ExperienceDate expDate) {
        em.merge(expDate);
    }

    public ExperienceDate retrieveExperienceDateByDateId(Long id) {
        return em.find(ExperienceDate.class, id);
    }

    public void deleteExperienceDate(Long id, String r) {
        ExperienceDate expDate = em.find(ExperienceDate.class, id);
        List<Booking> ls = expDate.getBookings();
        for (Booking b : ls) {
            b.setStatus(StatusEnum.CANCELLED);
            ExperienceDateCancellationReport rpt = new ExperienceDateCancellationReport();
            rpt.setBooking(b);
            rpt.setExperienceDate(expDate);
            rpt.setCancellationReason(r);
            experienceDateCancellationReportController.createNewExperienceDateCancellationReport(rpt);
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
