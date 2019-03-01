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
import entity.User;
import enumerated.StatusEnum;
import java.time.LocalDate;
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
public class ExperienceController implements ExperienceControllerRemote, ExperienceControllerLocal {

    @EJB
    private ExperienceDateCancellationReportControllerLocal experienceDateCancellationReportController;

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;

    public Experience createNewExperience(Experience exp) {
        em.persist(exp);
        em.flush();
        return exp;
    }

    public void updateExperienceInformation(Experience exp) {
        em.merge(exp);
    }

    public void deleteExperience(Long id, String r) {
        Experience exp = em.find(Experience.class, id);
        exp.setActive(false);
        LocalDate current = LocalDate.now();
        List<ExperienceDate> ll = exp.getExperienceDates();
        for (ExperienceDate date : ll) {
            if (date.getStartDate().compareTo(current) > 0) {
                List<Booking> ll2 = date.getBookings();
                for (Booking b : ll2) {
                    b.setStatus(StatusEnum.CANCELLED);
                    ExperienceDateCancellationReport rpt = new ExperienceDateCancellationReport();
                    rpt.setBooking(b);
                    rpt.setExperienceDate(date);
                    rpt.setCancellationReason(r);
                    experienceDateCancellationReportController.createNewExperienceDateCancellationReport(rpt);
                }
            }
        }
    }

    public Boolean removeFollowerFromExperience(Long id, User user) {
        Experience exp = em.find(Experience.class, id);
        return exp.getFollowers().remove(user);
    }

    public Boolean addFollowerToExperience(Long id, User user) {
        Experience exp = em.find(Experience.class, id);
        return exp.getFollowers().add(user);
    }

    public Experience retrieveExperienceById(Long id) {
        return em.find(Experience.class, id);
    }

    public void persist(Object object) {
        em.persist(object);
    }

}
