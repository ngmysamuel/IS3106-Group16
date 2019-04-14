/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Booking;
import entity.Evaluation;
import enumerated.StatusEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.BookingNotFoundException;
import util.exception.CreateNewEvaluationException;
import util.exception.InputDataValidationException;

/**
 *
 * @author samue
 */
@Stateless
public class EvaluationController implements EvaluationControllerLocal {

    @EJB
    private BookingControllerLocal bookingControllerLocal;
    
    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public EvaluationController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Evaluation createNewEvaluationFromHost(Evaluation newEvaluation, Long bookingId) throws InputDataValidationException, CreateNewEvaluationException {
        try {
            Booking booking = bookingControllerLocal.retrieveBookingByBookingId(bookingId);
            newEvaluation.setBooking(booking);
            
            Set<ConstraintViolation<Evaluation>> constraintViolations = validator.validate(newEvaluation);
            if (constraintViolations.isEmpty()) {
                booking.setEvaluationByHost(newEvaluation);
                booking.setUserEvaluated(true);
                
                em.persist(newEvaluation);
                em.flush();
                return newEvaluation;
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (BookingNotFoundException ex) {
            throw new CreateNewEvaluationException(ex.getMessage());
        }    
    }
    
    @Override
    public List<Evaluation> retrieveAllEvaluationsFromHostsByUserId(Long userId) {
        System.out.println("******** EvaluationController: retrieveAllEvaluationsFromHostsByUserId()");
        List<Evaluation> evaluations = new ArrayList();
        
        Query query = em.createQuery("SELECT e FROM Evaluation e WHERE e.booking.status = :inStatus AND e.booking.userEvaluated = true AND "
                + "e.booking.guest.userId = :inUserId");
        query.setParameter("inUserId", userId);
        query.setParameter("inStatus", StatusEnum.ACTIVE);
        evaluations = query.getResultList();
        System.out.println("**** evaluations.size: " + evaluations.size());
        System.out.println("--------------");
        return evaluations;
    }
    
    @Override
    public List<Evaluation> retrieveAllEvaluationsFromGuestsByUserId(Long userId) {
        List<Evaluation> evaluations = new ArrayList();
        
        Query query = em.createQuery("SELECT e FROM Evaluation e WHERE e.booking.status = :inStatus AND e.booking.hostEvaluated = true AND "
                + "e.booking.experienceDate.experience.host.userId = :inUserId");
        query.setParameter("inUserId", userId);
        query.setParameter("inStatus", StatusEnum.ACTIVE);
        evaluations = query.getResultList();
        
        return evaluations;
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Evaluation>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}