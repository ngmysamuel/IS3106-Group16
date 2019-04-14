/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.ExperienceDateCancellationReport;
import javax.ejb.Local;
import util.exception.InputDataValidationException;

/**
 *
 * @author samue
 */
@Local
public interface ExperienceDateCancellationReportControllerLocal {
    public ExperienceDateCancellationReport createNewExperienceDateCancellationReport(ExperienceDateCancellationReport rpt) throws InputDataValidationException;
}
