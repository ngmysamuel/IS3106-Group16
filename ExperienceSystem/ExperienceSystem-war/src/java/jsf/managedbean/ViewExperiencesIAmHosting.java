/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Experience;
import entity.ExperienceDate;
import entity.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;
import stateless.UserControllerLocal;
import util.exception.CreateNewExperienceDateException;
import util.exception.ExperienceNotActiveException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Named(value = "viewExperiencesIAmHosting")
@RequestScoped
public class ViewExperiencesIAmHosting {

    @EJB
    private ExperienceDateControllerLocal experienceDateController;

    @EJB
    private ExperienceControllerLocal experienceController;

    @EJB
    private UserControllerLocal userController;
    
    private User currentUser;
    private List<Experience> ls;
    private Experience currentExperience;
    
    private LocalDate start, end;
    private Integer capacity;
    private BigDecimal price;
    
    public ViewExperiencesIAmHosting() {
    }
    
    public void newExpDate(ActionEvent event) {
        currentExperience = (Experience) event.getComponent().getAttributes().get("exp");
        ExperienceDate ed = new ExperienceDate();
        ed.setActive(true);
        ed.setCapacity(capacity);
        ed.setExperience(currentExperience);
        ed.setSpotsAvailable(capacity);
        ed.setStartDate(start);
        ed.setEndDate(end);
        try {
            experienceDateController.createNewExperienceDate(ed);
        } catch (CreateNewExperienceDateException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong", null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong info", null));
        }
    }

    public void deleteExp(ActionEvent event) {
        Long id = (Long) event.getComponent().getAttributes().get("id");
        String reason = (String) event.getComponent().getAttributes().get("reason");
        try {
            experienceController.deleteExperience(id, reason);
        } catch (ExperienceNotActiveException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong", null));
        }
    }
    
    public void preUpdateExp(ActionEvent event) {
        currentExperience = (Experience) event.getComponent().getAttributes().get("expToUpdate");
    }
    
    public void updateExp() {
        try {
            experienceController.updateExperienceInformation(currentExperience);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Success", null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data entered is wrong", null));
        } catch (ExperienceNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Exp", null));
        }
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

    public List<Experience> getLs() {
        return ls;
    }

    public void setLs(List<Experience> ls) {
        this.ls = ls;
    }

    public Experience getCurrenExperience() {
        return currentExperience;
    }

    public void setCurrenExperience(Experience currenExperience) {
        this.currentExperience = currenExperience;
    }
    
}
