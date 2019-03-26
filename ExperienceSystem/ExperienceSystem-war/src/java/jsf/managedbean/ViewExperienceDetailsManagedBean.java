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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
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
@ViewScoped
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
    /**
     * Creates a new instance of ViewExperienceDetailsManagedBean
     */
    public ViewExperienceDetailsManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        experienceIdToView = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("productIdToView");
        try
        {            
            experienceEntityToView = experienceController.retrieveExperienceById(experienceIdToView);
            experienceDateEntities = experienceController.retrieveAllExperienceDates(experienceEntityToView);
            experienceFollowers = experienceEntityToView.getFollowers();
            if((boolean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")){
                isFollowed = experienceFollowers.contains((User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserEntity"));
            }
        }
        catch(ExperienceNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No experience with " + experienceIdToView + " found!", null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
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
    
}
