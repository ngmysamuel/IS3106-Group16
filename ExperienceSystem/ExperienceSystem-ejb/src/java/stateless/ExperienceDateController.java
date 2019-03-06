/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Booking;
import entity.Employee;
import entity.Experience;
import entity.ExperienceDate;
import entity.ExperienceDateCancellationReport;
import entity.ExperienceDatePaymentReport;
import enumerated.StatusEnum;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewEmployeeException;
import util.exception.CreateNewExperienceDateException;
import util.exception.ExperienceDateNotActiveException;
import util.exception.ExperienceDateNotFoundException;
import util.exception.InputDataValidationException;

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
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public ExperienceDateController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public ExperienceDate createNewExperienceDate(ExperienceDate newExperienceDate) throws CreateNewExperienceDateException, InputDataValidationException {
        Set<ConstraintViolation<ExperienceDate>> constraintViolations = validator.validate(newExperienceDate);
        
        if(constraintViolations.isEmpty()){
            try{
                em.persist(newExperienceDate);
                em.flush();
                return newExperienceDate;
            }catch(PersistenceException ex){
                if(ex.getCause() != null
                    && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewExperienceDateException("ExperienceDate already exists");
                }else{
                    throw new CreateNewExperienceDateException("An unexpected error has occurred: " + ex.getMessage());
                }
            }catch(Exception ex){
                throw new CreateNewExperienceDateException("An unexpected error has occurred " + ex.getMessage());
            }
        }else{
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public void updateExperienceDate(ExperienceDate expDate) {
        em.merge(expDate);
    }

    @Override
    public ExperienceDate retrieveExperienceDateByDateId(Long id) {
        return em.find(ExperienceDate.class, id);
    }
    
    public ExperienceDate retrieveExperienceDateByDate(Experience experience, Date startDate) throws ExperienceDateNotFoundException {
        List<ExperienceDate> dates = experience.getExperienceDates();
        for(ExperienceDate date: dates){
            Date start = java.sql.Date.valueOf(date.getStartDate());
            if(start.equals(startDate)){
                return date; 
            }
        }
        
        throw new ExperienceDateNotFoundException("Experience Date does not exist");
    }

    public List<ExperienceDate>  retrieveExperienceDatesOfAnExperience(Experience experience){
        Query query = em.createQuery("SELECT d FROM ExperienceDate d WHERE d.experience.experienceId = :inExperienceId");
        query.setParameter("inExperienceId", experience.getExperienceId());
        
        return query.getResultList();
    }
    
    @Override
    public void deleteExperienceDate(Long id, String r) throws ExperienceDateNotActiveException{
        ExperienceDate expDate = em.find(ExperienceDate.class, id);
        
        if(!expDate.isActive()){
            throw new ExperienceDateNotActiveException("This experienceDate is no longer active!");
        }else{
            expDate.setActive(false);
            
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
    }
    
    public List<ExperienceDateCancellationReport> retrieveAllExperienceDateCancellationReports(){
        Query query = em.createQuery("SELECT r FROM ExperienceDateCancellationReport r ORDER BY r.cancellationReportId");
        return query.getResultList();
    }
    
    public ExperienceDatePaymentReport retrieveExperienceDatePaymentReport(ExperienceDate experienceDate){
        Query query = em.createQuery("SELECT r FROM ExperienceDatePaymentReport r WHERE r.experienceDate.experienceDateId = :inExperienceDateId");
        query.setParameter("inExperienceDateId", experienceDate.getExperienceDateId());
        return (ExperienceDatePaymentReport)query.getResultList();
    }
    
    public void persist(Object object) {
        em.persist(object);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ExperienceDate>> constraintViolations) {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
