/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Category;
import entity.Experience;
import entity.Location;
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
@Named(value = "viewAllFavouriteManagedBean")
@ViewScoped
public class ViewAllFavouriteManagedBean implements Serializable {

    @EJB(name = "UserControllerLocal")
    private UserControllerLocal userControllerLocal;

    @EJB
    private ExperienceControllerLocal experienceControllerLocal;

    List<Experience> favouriteExperiences;
    private User currentUser;

    public ViewAllFavouriteManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("currentUser")) {
            currentUser = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
            favouriteExperiences = experienceControllerLocal.retrieveFavouriteExperiences(currentUser.getUserId());
        } else {
            System.out.println("******** ViewAllFavouriteManagedBean: the current user is null");
        }
    }

    public void viewFavouriteExperienceDetails(ActionEvent event) throws IOException {
        Experience favouriteExperienceToView = (Experience) event.getComponent().getAttributes().get("favouriteExperienceToView");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("favouriteExperienceToView", favouriteExperienceToView.getExperienceId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDetails.xhtml");
    }

    public void unfavouriteExperience(ActionEvent event) throws IOException {
        try {
            Experience unfavouriteExperience = (Experience) event.getComponent().getAttributes().get("unfavouriteExperience");
            experienceControllerLocal.removeFollowerFromExperience(unfavouriteExperience.getExperienceId(), currentUser.getUserId());
            favouriteExperiences.remove(unfavouriteExperience);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Experience unfavourite successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while unfavourite experience: " + ex.getMessage(), null));
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

}
