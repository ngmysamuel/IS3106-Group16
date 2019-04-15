/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Booking;
import entity.ExperienceDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.BookingNotFoundException;
import util.exception.CreateNewBookingException;
import util.exception.ExperienceDateNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author CaiYuqian
 */
@Stateless
public class BookingController implements BookingControllerLocal {

    @EJB
    private ExperienceDateControllerLocal experienceDateControllerLocal;
    
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
        System.out.println("******** BookingController: createNewBooking()");
        Set<ConstraintViolation<Booking>> constraintViolations = validator.validate(newBooking);
        
        if (constraintViolations.isEmpty()) {
            try {
                //set bidirectional relationship and update experience date slots information
                Integer numOfPeople = newBooking.getNumberOfPeople();
                System.out.println("**** numOfPeople: " + numOfPeople);
                ExperienceDate experienceDate = experienceDateControllerLocal.retrieveExperienceDateByExperienceDateId(newBooking.getExperienceDate().getExperienceDateId());
                System.out.println("**** this booking is made for experience date ID: " + experienceDate.getExperienceDateId());
                if (experienceDate.getSpotsAvailable() < numOfPeople) {
                    System.out.println("**** Not enough slots available");
                    throw new CreateNewBookingException("Not enough slots available");
                } else {
                    System.out.println("**** setting bidirecational relationships");
                    experienceDate.setSpotsAvailable(experienceDate.getSpotsAvailable() - numOfPeople);
                    experienceDate.getBookings().add(newBooking);

                    em.persist(newBooking);
                    em.flush();

                    return newBooking;
                }
            } catch(ExperienceDateNotFoundException ex) {
                throw new CreateNewBookingException("An error has occurred: " + ex.getMessage());
            }catch (Exception ex) {
                System.out.println("**** unexpected error....");
                ex.printStackTrace();
                throw new CreateNewBookingException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            System.out.println("**** InputDataValidationException");
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    // Retrieval
    
    @Override
    public Booking retrieveBookingByBookingId(Long id) throws BookingNotFoundException {
        Query query = em.createQuery("SELECT b FROM Booking b WHERE b.bookingId = :id");
        query.setParameter("id", id);
        try {
            Booking booking = (Booking)query.getSingleResult();
            return booking;
        } catch (NoResultException ex) {
            throw new BookingNotFoundException("Booking does not exist!");
        }
        
    }
    
    @Override
    public List<Booking> retrieveAllBookingsByGuestId(Long userId) {
        Query query = em.createQuery("SELECT b FROM Booking b WHERE b.guest.userId = :inUserId ORDER BY b.bookingId DESC");
        query.setParameter("inUserId", userId);
        return query.getResultList();
    }
    
    @Override
    public List<Booking> retrieveAllBookingsByExperienceId(Long experienceId) {
        Query query = em.createQuery("SELECT b FROM Booking b WHERE b.experienceDate.experience.experienceId = :experienceId ORDER BY b.bookingId DESC");
        query.setParameter("experienceId", experienceId);
        return query.getResultList();
    }
    
    @Override
    public void update(Booking b) throws InputDataValidationException { 
        Set<ConstraintViolation<Booking>> constraintViolations = validator.validate(b);
        if (constraintViolations.isEmpty()) {
            em.merge(b);
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
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
