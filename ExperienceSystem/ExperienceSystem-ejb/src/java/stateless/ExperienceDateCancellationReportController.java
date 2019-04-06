/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.ExperienceDateCancellationReport;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author samue
 */
@Stateless
public class ExperienceDateCancellationReportController implements ExperienceDateCancellationReportControllerLocal {

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

   public ExperienceDateCancellationReport createNewExperienceDateCancellationReport(ExperienceDateCancellationReport rpt) {
       em.persist(rpt);
       em.flush();
       return rpt;
   }
}
