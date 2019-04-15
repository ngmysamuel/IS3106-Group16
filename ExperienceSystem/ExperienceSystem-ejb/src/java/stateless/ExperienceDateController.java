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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
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
import util.exception.CreateNewExperienceDateException;
import util.exception.CreateNewExperienceException;
import util.exception.DeleteExperienceDateException;
import util.exception.ExperienceDateNotActiveException;
import util.exception.ExperienceDateNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author samue
 */
@Stateless
public class ExperienceDateController implements ExperienceDateControllerLocal {

    @EJB
    private ExperienceControllerLocal experienceControllerLocal;
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

    @Override
    public ExperienceDate createNewExperienceDate(ExperienceDate newExperienceDate) throws CreateNewExperienceDateException, InputDataValidationException {
        Set<ConstraintViolation<ExperienceDate>> constraintViolations = validator.validate(newExperienceDate);

        if (constraintViolations.isEmpty()) {
            try {
                // retrieve the experience and set the bidirectional relationship
                Experience experience = experienceControllerLocal.retrieveExperienceById(newExperienceDate.getExperience().getExperienceId());
                experience.getExperienceDates().add(newExperienceDate);
                
                em.persist(newExperienceDate); 
                em.flush();
                return newExperienceDate;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewExperienceDateException("ExperienceDate already exists");
                } else {
                    throw new CreateNewExperienceDateException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch (Exception ex) {
                throw new CreateNewExperienceDateException("An unexpected error has occurred " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public ExperienceDate updateExperienceDate(ExperienceDate expDate) throws InputDataValidationException, CreateNewExperienceException{
        Set<ConstraintViolation<ExperienceDate>> constraintViolations = validator.validate(expDate);
        
        if(constraintViolations.isEmpty()){
            try{
                em.merge(expDate);
                em.flush();
                return expDate;
            }catch(PersistenceException ex){
                if(ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewExperienceException("Experience with the same title already exists");
                }else{
                    throw new CreateNewExperienceException("An unexpected error has occurred: " + ex.getMessage());
                }
             } catch (Exception ex) {
                throw new CreateNewExperienceException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    @Override
    public ExperienceDate retrieveExperienceDateByExperienceDateId(Long experienceDateId) throws ExperienceDateNotFoundException {
        ExperienceDate experienceDate = em.find(ExperienceDate.class, experienceDateId);
        for (Booking booking : experienceDate.getBookings()) {
            booking.getGuest();
        }
        return experienceDate;
    }

    @Override
    public List<ExperienceDate> retrieveExperienceDatesOfAnExperience(Experience experience) {
        Query query = em.createQuery("SELECT d FROM ExperienceDate d WHERE d.experience.experienceId = :inExperienceId");
        query.setParameter("inExperienceId", experience.getExperienceId());

        return query.getResultList();
    }
  
    @Override
    public List<ExperienceDate> retrieveAllActiveExperienceDatesByExperienceId(Long experienceId) {
        List<ExperienceDate> activeExperienceDates = new ArrayList();
        
        Experience experience = em.find(Experience.class, experienceId);
        if(experience != null) {
            activeExperienceDates = experience.getExperienceDates();
            ListIterator iter = activeExperienceDates.listIterator();
            while(iter.hasNext()){
                ExperienceDate currentExperienceDate = (ExperienceDate)iter.next();
                if(!currentExperienceDate.isActive()) {
                    iter.remove();
                }
            }
            return activeExperienceDates;
        } else {
            return activeExperienceDates;
        }
    }
    
    @Override
    public List<ExperienceDate> retrieveAllInactiveExperienceDatesByExperienceId(Long experienceId) {
        List<ExperienceDate> inactiveExperienceDates = new ArrayList();
        
        Experience experience = em.find(Experience.class, experienceId);
        if(experience != null) {
            inactiveExperienceDates = experience.getExperienceDates();
            ListIterator iter = inactiveExperienceDates.listIterator();
            while(iter.hasNext()){
                ExperienceDate currentExperienceDate = (ExperienceDate)iter.next();
                if(currentExperienceDate.isActive()) {
                    iter.remove();
                } else {
                    currentExperienceDate.getBookings().size();
                }
            }
            return inactiveExperienceDates;
        } else {
            return inactiveExperienceDates;
        }
    }
    
    @Override
    public List<User> retrieveAllGuestsByExperienceDateId(Long experienceDateId) {
        System.out.println("******** ExperienceDateController: retrieveAllGuestsByExperienceDateId()");
        List<User> guests = new ArrayList();
        
        ExperienceDate experienceDate = em.find(ExperienceDate.class, experienceDateId);
        if(experienceDate != null) {
            System.out.println("**** experience date: " + experienceDate.getExperienceDateId());
            for(Booking booking: experienceDate.getBookings()) {
                guests.add(booking.getGuest());
            }
            System.out.println("**** guest size: " + guests.size());
        }
        System.out.println("-----------------------------");
        return guests;
    }

    

    @Override
    public void deleteExperienceDate(Long experienceDateId, String reason) throws ExperienceDateNotActiveException, DeleteExperienceDateException {
        ExperienceDate experienceDate = em.find(ExperienceDate.class, experienceDateId);

        if (!experienceDate.isActive()) {
            throw new ExperienceDateNotActiveException("This experienceDate is already inactive!");
        } else {
            experienceDate.setActive(false);
            // create an experience date cancellation report for all the associated bookings
            List<Booking> bookings = experienceDate.getBookings();
            for (Booking booking : bookings) {
                if (booking.getStatus().equals(StatusEnum.ACTIVE)) {
                    ExperienceDateCancellationReport report = new ExperienceDateCancellationReport();
                    report.setBooking(booking);
                    report.setExperienceDate(experienceDate);
                    report.setCancellationReason(reason);
                    report.setReportGenerationDateTime(new Date());
                    booking.setStatus(StatusEnum.CANCELLED);
                    booking.setCancellationReport(report);
                    try {
                        experienceDateCancellationReportController.createNewExperienceDateCancellationReport(report);
                    } catch (InputDataValidationException ex) {
                        throw new DeleteExperienceDateException(ex.getMessage());
                    }
                }
            }
            // create an experience date cancellation report for the experience date
            ExperienceDateCancellationReport report = new ExperienceDateCancellationReport();
            report.setExperienceDate(experienceDate);
            report.setCancellationReason(reason);
            report.setReportGenerationDateTime(new Date());
            experienceDate.setExperienceDateCancellationReport(report);
            try {
                experienceDateCancellationReportController.createNewExperienceDateCancellationReport(report);
            } catch (InputDataValidationException ex) {
                throw new DeleteExperienceDateException(ex.getMessage());
            }
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ExperienceDate>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}