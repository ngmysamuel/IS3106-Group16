/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.ExperienceDateCancellationReport;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;

/**
 *
 * @author samue
 */
@Stateless
public class ExperienceDateCancellationReportController implements ExperienceDateCancellationReportControllerLocal {

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public ExperienceDateCancellationReportController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    } 

    @Override
    public ExperienceDateCancellationReport createNewExperienceDateCancellationReport(ExperienceDateCancellationReport rpt) throws InputDataValidationException {
        Set<ConstraintViolation<ExperienceDateCancellationReport>> constraintViolations = validator.validate(rpt);
        if(constraintViolations.isEmpty()) {
            em.persist(rpt);
            em.flush();
            return rpt;
        } else {
            System.out.println("**** InputDataValidationException");
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
        
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ExperienceDateCancellationReport>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
