/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Booking;
import entity.ExperienceDate;
import entity.Message;
import entity.User;
import enumerated.StatusEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.BookingControllerLocal;
import stateless.ExperienceDateControllerLocal;
import stateless.UserControllerLocal;
import util.exception.CreateNewExperienceException;
import util.exception.InputDataValidationException;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Named(value = "viewAllMyReservations")
@RequestScoped
public class ViewAllMyReservations {

    @EJB
    private ExperienceDateControllerLocal experienceDateController;

    @EJB
    private BookingControllerLocal bookingController;

    @EJB
    private UserControllerLocal userController;
    
    private List<Booking> ls;
    private List<Booking> filteredListOExperiences;
    
    private User currentUser;
    
    public ViewAllMyReservations() {
    }
    
    public void commHost(ActionEvent event) {
        String messageString = (String) event.getComponent().getAttributes().get("message");
        User host = (User) event.getComponent().getAttributes().get("host");
        Message message = new Message();
        message.setContent(messageString);
        message.setSender(currentUser);
        message.setRecipient(host);
        message.setSendDate(LocalDateTime.now());
        currentUser.getMessagesSent().add(message);
        userController.update(currentUser);
        host.getMessagesSent().add(message);
        userController.update(host);
    }
    
    public boolean filterByPrice(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        return ((Comparable) value).compareTo(BigDecimal.valueOf(Integer.valueOf(filterText))) > 0;
    }
    
    public void cancelReservation(ActionEvent event) {
        Booking booking = (Booking) event.getComponent().getAttributes().get("bookingToCancel");
        User u = currentUser;
        List<Booking> bookings = u.getBookings();
        for (Booking b : bookings) {
            if (Objects.equals(b.getBookingId(), booking.getBookingId())) {
                b.setStatus(StatusEnum.CANCELLED);
                break;
            }   
        }
        u.setBookings(bookings);
        userController.update(u);
        ExperienceDate ed = booking.getExperienceDate();
        for (Booking b : ed.getBookings()) {
            if (Objects.equals(b.getBookingId(), booking.getBookingId())) {
                b.setStatus(StatusEnum.CANCELLED);
                break;
            }
        }
        try {
            experienceDateController.updateExperienceDate(ed);
        } catch (InputDataValidationException ex) {
            Logger.getLogger(ViewAllMyReservations.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CreateNewExperienceException ex) {
            Logger.getLogger(ViewAllMyReservations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Booking> getLs() {
        List<Booking> ls = currentUser.getBookings();
        List<Booking> ls2 = new LinkedList<>();
        for (Booking b  : ls) {
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (b.getExperienceDate().getStartDate().after(date)) {
                ls2.add(b);
            }
        }
        return ls2;
    }

    public void setLs(List<Booking> ls) {
        this.ls = ls;
    }

    public List<Booking> getFilteredListOExperiences() {
        return filteredListOExperiences;
    }

    public void setFilteredListOExperiences(List<Booking> filteredListOExperiences) {
        this.filteredListOExperiences = filteredListOExperiences;
    }

    public User getCurrentUser() {
         User u = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustEntity");
        try {
            u = userController.retrieveUserByUsername(u.getUsername());
        } catch (UserNotFoundException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "wrong", null));
        }
        return u;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    
}
