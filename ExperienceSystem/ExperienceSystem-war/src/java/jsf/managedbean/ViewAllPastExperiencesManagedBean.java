/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Booking;
import entity.Evaluation;
import entity.Experience;
import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import stateless.BookingControllerLocal;
import stateless.EvaluationControllerLocal;
import stateless.ExperienceControllerLocal;
import stateless.UserControllerLocal;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Named(value = "viewAllPastExperiencesManagedBean")
@ViewScoped
public class ViewAllPastExperiencesManagedBean implements Serializable{

    @EJB(name = "ExperienceControllerLocal")
    private ExperienceControllerLocal experienceControllerLocal;
    
    private List<Experience> pastExperiences;
    private User currentUser;
    
    public ViewAllPastExperiencesManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        currentUser = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        pastExperiences = experienceControllerLocal.retrievePastExperiences(currentUser.getUserId());
    }
    
    public void viewPastExperienceDetails(ActionEvent event) throws IOException{
        Experience pastExperienceToView = (Experience)event.getComponent().getAttributes().get("pastExperienceToView");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("experienceToView", pastExperienceToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDetails.xhtml");
    }

    public List<Experience> getPastExperiences() {
        return pastExperiences;
    }

    public void setPastExperiences(List<Experience> pastExperiences) {
        this.pastExperiences = pastExperiences;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    
    
}
