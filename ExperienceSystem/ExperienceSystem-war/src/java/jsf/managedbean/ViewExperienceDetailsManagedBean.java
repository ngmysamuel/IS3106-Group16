/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Booking;
import entity.Experience;
import entity.ExperienceDate;
import entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import stateless.BookingControllerLocal;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;
import util.exception.CreateNewBookingException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author Asus
 */
@Named(value = "viewExperienceDetailsManagedBean")
@RequestScoped
public class ViewExperienceDetailsManagedBean {

    @EJB
    private BookingControllerLocal bookingController;

    @EJB
    private ExperienceDateControllerLocal experienceDateController;

    @EJB
    private ExperienceControllerLocal experienceController;
    private Long experienceIdToView;
    private Experience experienceEntityToView;
    private List<ExperienceDate> experienceDateEntities;
    private List<User> experienceFollowers;
    private Booking newBooking;
    private Boolean isFollowed;

    private List<String> images;
    /**
     * Creates a new instance of ViewExperienceDetailsManagedBean
     */
    public ViewExperienceDetailsManagedBean() {
        images = new ArrayList();
        images.add("https://i.imgur.com/2hocHvd.jpg");
        images.add("https://i.imgur.com/R6wtAtN.jpg");
        images.add("https://i.imgur.com/slbHL1Z.jpg");
        images.add("https://i.imgur.com/SQiLyd4.jpg");
    }
    
    @PostConstruct
    public void postConstruct(){
        experienceEntityToView = (Experience)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("experienceEntityToView");
    }
    
    public void reserveExperienceDate(){
        try{
            bookingController.createNewBooking(newBooking);
        } catch(CreateNewBookingException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occured while entering your booking: " + ex.getMessage(), null));
        } catch(InputDataValidationException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "You have inputted wrong data for the booking: " + ex.getMessage(), null));
        } catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occured: " + ex.getMessage(), null));
        }
    }
    
    
    public void addFavoriteExperience(){
        experienceController.addFollowerToExperience(experienceIdToView, (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserEntity"));
        this.isFollowed = true;
    }
    
    public void removeFavoriteExperience(){
        experienceController.removeFollowerFromExperience(experienceIdToView, (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserEntity"));
        this.isFollowed = false;
    }
    
    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Long getExperienceIdToView() {
        return experienceIdToView;
    }

    public void setExperienceIdToView(Long experienceIdToView) {
        this.experienceIdToView = experienceIdToView;
    }

    public Experience getExperienceEntityToView() {
        return experienceEntityToView;
    }

    public void setExperienceEntityToView(Experience experienceEntityToView) {
        this.experienceEntityToView = experienceEntityToView;
    }

    public List<ExperienceDate> getExperienceDateEntities() {
        return experienceDateEntities;
    }

    public void setExperienceDateEntities(List<ExperienceDate> experienceDateEntities) {
        this.experienceDateEntities = experienceDateEntities;
    }

    public List<User> getExperienceFollowers() {
        return experienceFollowers;
    }

    public void setExperienceFollowers(List<User> experienceFollowers) {
        this.experienceFollowers = experienceFollowers;
    }

    public Booking getNewBooking() {
        return newBooking;
    }

    public void setNewBooking(Booking newBooking) {
        this.newBooking = newBooking;
    }

    public Boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(Boolean isFollowed) {
        this.isFollowed = isFollowed;
    }
    
}
