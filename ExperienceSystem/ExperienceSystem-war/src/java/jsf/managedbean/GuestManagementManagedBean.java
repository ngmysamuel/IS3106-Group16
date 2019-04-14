/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Booking;
import entity.Evaluation;
import entity.Experience;
import entity.ExperienceDate;
import entity.User;
import enumerated.StatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.BookingControllerLocal;
import stateless.EvaluationControllerLocal;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;
import util.exception.BookingNotFoundException;
import util.exception.CreateNewEvaluationException;
import util.exception.InputDataValidationException;

/**
 *
 * @author CaiYuqian
 */
@Named(value = "guestManagementManagedBean")
@ViewScoped
public class GuestManagementManagedBean implements Serializable {

    @EJB
    private ExperienceControllerLocal experienceControllerLocal;
    @EJB
    private ExperienceDateControllerLocal experienceDateControllerLocal;
    @EJB
    private BookingControllerLocal bookingControllerLocal;
    @EJB
    private EvaluationControllerLocal evaluationControllerLocal;
    
    private User currentUser;
    private Long userIdToView;
    
    private Long bookingIdToWriteReview;
    private Long pendingExperienceDateIdToWriteReview;
    private User selectedGuest;
    private Long bookingIdToViewReview;
    private Evaluation selectedEvaluationToView;
    
    private Evaluation newEvaluation;
    private Integer guestRating;
    private String comment;
    
    // experience dates ready for evaluation must have already happened (future experience date will not appear here)
    // pending experience dates contains bookings that have not been evaluated by the host
    private List<ExperienceDate> pendingExperienceDates;
    // pending experience dates contains bookings that have already been evaluated by the host
    private List<ExperienceDate> pastExperienceDates;
    
    public GuestManagementManagedBean() {
        pendingExperienceDates = new ArrayList();
        pastExperienceDates = new ArrayList();
    }
    
    @PostConstruct
    public void postConstruct() {
        System.out.println("******** GuestManagementManagedBean: postConstruct()");
        configureCurrentUser();
        initializeEvaluationStatus();
        if(currentUser != null) {
            refreshExperienceDatesEvaluationsInformation();
        }
        System.out.println("-----------------------------");
    }
    
    public void createNewGuestReview(ActionEvent event) {
        if(guestRating == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Rating is required!", null));
        } else if(bookingIdToWriteReview == null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Booking information is unavailable!", null));
        } else {
            newEvaluation.setScore(new BigDecimal(guestRating));
            newEvaluation.setRemark(comment);
            newEvaluation.setUserEvaluating(currentUser);
            newEvaluation.setEvaluationTime(new Date());
            try{
                evaluationControllerLocal.createNewEvaluationFromHost(newEvaluation, bookingIdToWriteReview);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Evaluation made successfully! Evaluation ID: " + newEvaluation.getEvaluationId(), null));
                refreshExperienceDatesEvaluationsInformation();
            } catch(InputDataValidationException | CreateNewEvaluationException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred: " + ex.getMessage(), null));
            }
        }
        
    }
    
    public void viewHostReview(ActionEvent event) {
        Long bookingIdToViewReview = (Long)event.getComponent().getAttributes().get("bookingIdToViewReview");
        if(bookingIdToViewReview == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Booking information is invalid!", null));
        } else {
            try {
                Booking selectedBooking = bookingControllerLocal.retrieveBookingByBookingId(bookingIdToViewReview);
                selectedEvaluationToView = selectedBooking.getEvaluationByHost();
            } catch (BookingNotFoundException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred: " + ex.getMessage(), null));
            }
        }
    }
    
    public String viewOtherUserInfo() {        
        System.out.println("******** GuestManagementManagedBean: viewOtherUserInfo()");
        System.out.println("-----------------------------");
        if(currentUser != null && currentUser.getUserId().equals(userIdToView)) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("previousPage", "guestManagement.xhtml");
            return "accountInfo.xhtml";
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("previousPage", "guestManagement.xhtml");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("userIdToView", userIdToView);
        }
        return "viewOtherUserInfo.xhtml";
    }
    
    private ExperienceDate cloneExperienceDate(ExperienceDate originalExperienceDate) {
        ExperienceDate clone = new ExperienceDate();
        clone.setExperienceDateId(originalExperienceDate.getExperienceDateId());
        clone.setStartDate(originalExperienceDate.getStartDate());
        clone.setExperience(originalExperienceDate.getExperience());
        return clone;
    }
    
    public void initializeEvaluationStatus() {
        newEvaluation = new Evaluation();
        guestRating = 1;
        comment = null;
    }
    
    private void refreshExperienceDatesEvaluationsInformation() {
        System.out.println("******** GuestManagementManagedBean: refreshExperienceDatesEvaluationsInformation()");
        
        pendingExperienceDates = new ArrayList();
        pastExperienceDates = new ArrayList();

        // configure the pending and past experience dates
        List<Experience> hostExperiences = experienceControllerLocal.retrieveAllActiveHostExperiencesByHostId(currentUser.getUserId());
        System.out.println("**** the user has " + hostExperiences.size() + " active host experiences");
        for (Experience experience : hostExperiences) {
            System.out.println("...........");
            System.out.println("**** experience: " + experience.getTitle());
            List<ExperienceDate> inactiveExperienceDates = experienceDateControllerLocal.retrieveAllInactiveExperienceDatesByExperienceId(experience.getExperienceId());
            System.out.println("**** this experience has " + inactiveExperienceDates.size() + " inactive ExperienceDate");
            // check bookings for every inactive experience dates
            for (ExperienceDate inactiveExperienceDate : inactiveExperienceDates) {
                System.out.println("......");
                System.out.println("**** inactiveExperienceDate ID: " + inactiveExperienceDate.getExperienceDateId());
                System.out.println("**** this experience date has " + inactiveExperienceDate.getBookings().size() + " bookings in total");
                Boolean hasPendingEvaluation = false;
                Boolean hasPastEvaluation = false;
                ExperienceDate pastExperienceDate = cloneExperienceDate(inactiveExperienceDate);
                ExperienceDate pendingExperienceDate = cloneExperienceDate(inactiveExperienceDate);
                for (Booking booking : inactiveExperienceDate.getBookings()) {
                    if (!booking.getStatus().equals(StatusEnum.CANCELLED) && booking.isUserEvaluated()) {
                        // add the experience date into pastExperienceDates
                        pastExperienceDate.getBookings().add(booking);
                        hasPastEvaluation = true;
                    } else if (!booking.getStatus().equals(StatusEnum.CANCELLED) && !booking.isUserEvaluated()) {
                        // add the experience date into pendingExperienceDates
                        pendingExperienceDate.getBookings().add(booking);
                        hasPendingEvaluation = true;
                    }
                }
                if (hasPendingEvaluation) {
                    System.out.println("**** this experience date has pending evaluations");
                    pendingExperienceDates.add(pendingExperienceDate);
                }
                if (hasPastEvaluation) {
                    System.out.println("**** this experience date has past evaluations");
                    pastExperienceDates.add(pastExperienceDate);
                }
            }
        }
        System.out.println("-----------------------------");
    }
    
    public void configureCurrentUser() {
        Boolean isLogin = (Boolean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin");
        if(isLogin != null && isLogin){
            currentUser = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        }
    }

    public String convertDateToString(Date date) {
        if(date != null) {
            SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");   
            return formatter.format(date);
        } else {
            return null;
        } 
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<ExperienceDate> getPendingExperienceDates() {
        return pendingExperienceDates;
    }

    public void setPendingExperienceDates(List<ExperienceDate> pendingExperienceDates) {
        this.pendingExperienceDates = pendingExperienceDates;
    }

    public List<ExperienceDate> getPastExperienceDates() {
        return pastExperienceDates;
    }

    public void setPastExperienceDates(List<ExperienceDate> pastExperienceDates) {
        this.pastExperienceDates = pastExperienceDates;
    }

    public Long getUserIdToView() {
        return userIdToView;
    }

    public void setUserIdToView(Long userIdToView) {
        this.userIdToView = userIdToView;
    }

    public Long getBookingIdToWriteReview() {
        return bookingIdToWriteReview;
    }

    public void setBookingIdToWriteReview(Long bookingIdToWriteReview) {
        this.bookingIdToWriteReview = bookingIdToWriteReview;
    }

    public Integer getGuestRating() {
        return guestRating;
    }

    public void setGuestRating(Integer guestRating) {
        this.guestRating = guestRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Evaluation getNewEvaluation() {
        return newEvaluation;
    }

    public void setNewEvaluation(Evaluation newEvaluation) {
        this.newEvaluation = newEvaluation;
    }

    public Long getPendingExperienceDateIdToWriteReview() {
        return pendingExperienceDateIdToWriteReview;
    }

    public void setPendingExperienceDateIdToWriteReview(Long pendingExperienceDateIdToWriteReview) {
        this.pendingExperienceDateIdToWriteReview = pendingExperienceDateIdToWriteReview;
    }

    public User getSelectedGuest() {
        return selectedGuest;
    }

    public void setSelectedGuest(User selectedGuest) {
        this.selectedGuest = selectedGuest;
    }

    public Long getBookingIdToViewReview() {
        return bookingIdToViewReview;
    }

    public void setBookingIdToViewReview(Long bookingIdToViewReview) {
        this.bookingIdToViewReview = bookingIdToViewReview;
    }

    public Evaluation getSelectedEvaluationToView() {
        return selectedEvaluationToView;
    }

    public void setSelectedEvaluationToView(Evaluation selectedEvaluationToView) {
        this.selectedEvaluationToView = selectedEvaluationToView;
    }

}
