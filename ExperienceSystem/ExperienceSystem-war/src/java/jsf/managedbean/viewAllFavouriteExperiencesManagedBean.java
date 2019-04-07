/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Experience;
import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.ExperienceControllerLocal;
import stateless.UserControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewAllFavouriteExperiencesManagedBean")
@ViewScoped
public class viewAllFavouriteExperiencesManagedBean implements Serializable{

    @EJB
    private UserControllerLocal userControllerLocal;

    @EJB
    private ExperienceControllerLocal experienceControllerLocal;
    
    private List<Experience> favouriteExperiences;
    private User currentUser; 
    
    private Experience favouriteExperienceToView; 
    
    private Experience experienceToUnfavourite; 
    public viewAllFavouriteExperiencesManagedBean() {
    }
    
    @PostConstruct 
    public void PostConstruct(){
        System.out.println("**** ViewAllFavouriteExperiencesManagedBean: postConstruct()");
        currentUser = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        favouriteExperiences = experienceControllerLocal.retrieveAllFavouriteExperienceByUserId(currentUser.getUserId());
        System.out.println("****hostExperiences.size "+favouriteExperiences.size());
    }
    
    public void viewFavouriteExperiencesDetails(ActionEvent event) throws IOException{
        favouriteExperienceToView = (Experience)event.getComponent().getAttributes().get("favouriteExperienceToView");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("experienceToView", favouriteExperienceToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDetails.xhtml");
    }
    
    public void unfavouriteExperience(ActionEvent event) throws IOException{
        try{
            
            experienceToUnfavourite = (Experience) event.getComponent().getAttributes().get("experienceToUnfavourite");
            
            experienceControllerLocal.removeFollowerFromExperience(experienceToUnfavourite.getExperienceId(), currentUser.getUserId());
            
            favouriteExperiences.remove(experienceToUnfavourite);
            System.out.println("Did you reach here??");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Experience Unfollow successfully", null));
            System.out.println("Why are you so silly");
            
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<Experience> getFavouriteExperiences() {
        return favouriteExperiences;
    }

    public void setFavouriteExperiences(List<Experience> favouriteExperiences) {
        this.favouriteExperiences = favouriteExperiences;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Experience getFavouriteExperienceToView() {
        return favouriteExperienceToView;
    }

    public void setFavouriteExperienceToView(Experience favouriteExperienceToView) {
        this.favouriteExperienceToView = favouriteExperienceToView;
    }

    public Experience getExperienceToUnfavourite() {
        return experienceToUnfavourite;
    }

    public void setExperienceToUnfavourite(Experience experienceToUnfavourite) {
        this.experienceToUnfavourite = experienceToUnfavourite;
    }    
}
