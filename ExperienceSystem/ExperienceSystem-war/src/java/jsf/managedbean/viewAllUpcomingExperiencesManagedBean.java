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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.ExperienceControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewAllUpcomingExperiencesManagedBean")
@ViewScoped
public class viewAllUpcomingExperiencesManagedBean implements Serializable{

    @EJB(name = "ExperienceControllerLocal")
    private ExperienceControllerLocal experienceControllerLocal;

    private List<Experience> upcomingExperiences;
    private User currentUser; 
    
    public viewAllUpcomingExperiencesManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        currentUser = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        upcomingExperiences = experienceControllerLocal.retrieveUpcomingExperiences(currentUser.getUserId());
    }
    
    public void viewUpcomingExperienceDetails(ActionEvent event) throws IOException{
        Experience upcomingExperienceToView = (Experience)event.getComponent().getAttributes().get("upcomingExperienceToView");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("experienceToView", upcomingExperienceToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDetails.xhtml");
    }

    public List<Experience> getUpcomingExperiences() {
        return upcomingExperiences;
    }

    public void setUpcomingExperiences(List<Experience> upcomingExperiences) {
        this.upcomingExperiences = upcomingExperiences;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    
    
    
    
}
