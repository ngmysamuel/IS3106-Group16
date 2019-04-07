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
import entity.ExperienceDatePaymentReport;
import enumerated.StatusEnum;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import static java.util.Locale.filter;
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

    public ExperienceDate createNewExperienceDate(ExperienceDate newExperienceDate) throws CreateNewExperienceDateException, InputDataValidationException {
        Set<ConstraintViolation<ExperienceDate>> constraintViolations = validator.validate(newExperienceDate);

        if (constraintViolations.isEmpty()) {
            try {
                // retrieve the experience and set the bidirectional relationship
                Experience experience = experienceControllerLocal.retrieveExperienceById(newExperienceDate.getExperience().getExperienceId());
                
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
    public List<ExperienceDate> retrieveAllExperienceDates(){
        Query query = em.createQuery("SELECT ed FROM ExperienceDate ed");
        List<ExperienceDate> eds = query.getResultList();
        if(eds == null || eds.isEmpty() || eds.get(0) == null){
            return new ArrayList<>();
        }
        for(ExperienceDate ed: eds){
            ed.getExperience();
        }
        return eds;
    }
    
    @Override
    public ExperienceDate retrieveExperienceDateByDateId(Long id) {
        ExperienceDate ed = em.find(ExperienceDate.class, id);
        for (Booking b : ed.getBookings()) {
            b.getGuest().getUsername();
        }
        return ed;
    }

    public ExperienceDate retrieveExperienceDateByDate(Experience experience, Date startDate) throws ExperienceDateNotFoundException {
        List<ExperienceDate> dates = experience.getExperienceDates();
        for (ExperienceDate date : dates) {
            Date start = date.getStartDate();
            if (start.equals(startDate)) {
                return date;
            }
        }

        throw new ExperienceDateNotFoundException("Experience Date does not exist");
    }

    public List<ExperienceDate> retrieveExperienceDatesOfAnExperience(Experience experience) {
        Query query = em.createQuery("SELECT d FROM ExperienceDate d WHERE d.experience.experienceId = :inExperienceId");
        query.setParameter("inExperienceId", experience.getExperienceId());

        List<ExperienceDate> experienceDates = query.getResultList();
        if(experienceDates == null || experienceDates.isEmpty() || experienceDates.get(0) == null){
            return new ArrayList<>();
        }
        for(ExperienceDate ed: experienceDates){
            ed.getBookings();
            ed.getExperience();
        }
        return experienceDates;
    }

    // TO CHECK: whether after an experience date is cancelled, it has been automatically removed from the association for the experience
    @Override
    public void deleteExperienceDate(Long experienceDateId, String reason) throws ExperienceDateNotActiveException {
        ExperienceDate experienceDate = em.find(ExperienceDate.class, experienceDateId);

        if (!experienceDate.isActive()) {
            throw new ExperienceDateNotActiveException("This experienceDate is already inactive!");
        } else {
            experienceDate.setActive(false);
            
            List<Booking> bookings = experienceDate.getBookings();
            for (Booking booking : bookings) {
                booking.setStatus(StatusEnum.CANCELLED);
                ExperienceDateCancellationReport report = new ExperienceDateCancellationReport();
                report.setBooking(booking);
                report.setExperienceDate(experienceDate);
                report.setCancellationReason(reason);
                experienceDateCancellationReportController.createNewExperienceDateCancellationReport(report);
            }
        }
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

    public List<ExperienceDateCancellationReport> retrieveAllExperienceDateCancellationReports() {
        Query query = em.createQuery("SELECT r FROM ExperienceDateCancellationReport r ORDER BY r.cancellationReportId");
        return query.getResultList();
    }

    public ExperienceDatePaymentReport retrieveExperienceDatePaymentReport(ExperienceDate experienceDate) {
        Query query = em.createQuery("SELECT r FROM ExperienceDatePaymentReport r WHERE r.experienceDate.experienceDateId = :inExperienceDateId");
        query.setParameter("inExperienceDateId", experienceDate.getExperienceDateId());
        return (ExperienceDatePaymentReport) query.getResultList();
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ExperienceDate>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
