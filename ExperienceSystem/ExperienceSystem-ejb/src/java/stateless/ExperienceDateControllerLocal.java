/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Experience;
import entity.ExperienceDate;
import entity.ExperienceDateCancellationReport;
import entity.ExperienceDatePaymentReport;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewExperienceDateException;
import util.exception.ExperienceDateNotActiveException;
import util.exception.ExperienceDateNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author samue
 */
@Local
public interface ExperienceDateControllerLocal {
    public void updateExperienceDate(ExperienceDate expDate);
    public ExperienceDate retrieveExperienceDateByDateId(Long id);
    

    public ExperienceDate createNewExperienceDate(ExperienceDate newExperienceDate) throws CreateNewExperienceDateException, InputDataValidationException;

    public ExperienceDate retrieveExperienceDateByDate(Experience experience, Date startDate) throws ExperienceDateNotFoundException;

    public void deleteExperienceDate(Long id, String r) throws ExperienceDateNotActiveException;

    public ExperienceDatePaymentReport retrieveExperienceDatePaymentReport(ExperienceDate experienceDate);

    public List<ExperienceDateCancellationReport> retrieveAllExperienceDateCancellationReports();
}
