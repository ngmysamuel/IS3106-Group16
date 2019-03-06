/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Booking;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewBookingException;
import util.exception.InputDataValidationException;

/**
 *
 * @author CaiYuqian
 */
@Stateless
public class BookingController implements BookingControllerRemote, BookingControllerLocal {

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public BookingController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Booking createNewBooking(Booking newBooking) throws CreateNewBookingException, InputDataValidationException {
        Set<ConstraintViolation<Booking>> constraintViolations = validator.validate(newBooking);
        
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newBooking);
                em.flush();
                return newBooking;
            } catch (Exception ex) {
                throw new CreateNewBookingException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<Booking> retrieveAllBookingsByGuestId(Long userId) {
        Query query = em.createQuery("SELECT b FROM Booking b WHERE b.user.userId = :inUserId ORDER BY b.bookingId DESC");
        query.setParameter("inUserId", userId);
        return query.getResultList();
    }
    
    @Override
    public List<Booking> retrieveAllUpcomingBookingsByGuestId(Long userId) {
        Query query = em.createQuery("SELECT b FROM Booking b WHERE b.user.userId = :inUserId AND b.experienceDate.startDate > :currentMoment BY b.bookingId DESC");
        query.setParameter("inUserId", userId);
        query.setParameter("currentMoment", new Date(), TemporalType.TIMESTAMP);
        return query.getResultList();
    }
    
    @Override
    public List<Booking> retrieveAllBookingsByExperienceId(Long experienceId) {
        Query query = em.createQuery("SELECT b FROM Booking b WHERE b.experienceDate.experience.experienceId = :experienceId ORDER BY b.bookingId DESC");
        query.setParameter("experienceId", experienceId);
        return query.getResultList();
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Booking>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}