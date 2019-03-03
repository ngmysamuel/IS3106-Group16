/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Booking;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CreateNewBookingException;
import util.exception.InputDataValidationException;

/**
 *
 * @author CaiYuqian
 */
@Remote
public interface BookingControllerRemote {
    public Booking createNewBooking(Booking newBooking) throws CreateNewBookingException, InputDataValidationException;
    
    public List<Booking> retrieveAllBookingsByGuestId(Long userId);
    
    public List<Booking> retrieveAllUpcomingBookingsByGuestId(Long userId);
    
    public List<Booking> retrieveAllBookingsByExperienceId(Long experienceId);
}
