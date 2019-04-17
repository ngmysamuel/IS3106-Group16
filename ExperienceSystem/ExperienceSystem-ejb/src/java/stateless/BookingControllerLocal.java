/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Booking;
import java.util.List;
import javax.ejb.Local;
import util.exception.BookingNotFoundException;
import util.exception.CreateNewBookingException;
import util.exception.InputDataValidationException;

/**
 *
 * @author CaiYuqian
 */
@Local
public interface BookingControllerLocal {
    public Booking createNewBooking(Booking newBooking) throws CreateNewBookingException, InputDataValidationException;
    
    public Booking retrieveBookingByBookingId(Long id) throws BookingNotFoundException;
    
    public List<Booking> retrieveAllBookingsByGuestId(Long userId);
    
    public List<Booking> retrieveAllBookingsByExperienceId(Long experienceId);
    
    public void update(Booking b) throws InputDataValidationException;

    public List<Booking> retrieveAllBookingsByExperienceDateId(Long experienceDateId);
}
