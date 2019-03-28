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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
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
    
    

    
    public ViewExperiencesIAmHosting() {
    }
    
    
    public void nextAvailDate() {
        
    }

    
    
  
    
    public User getCurrentUser() {
        User u = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
System.out.println(u+"---------------------------");
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
        ls = new ArrayList<>();
        List<Experience> ls2 = getCurrentUser().getExperienceHosted();
        for (Experience e : ls2) {
            if (e.isActive()) {
                ls.add(e);
            }
        }
        return ls;
    }

    public void setLs(List<Experience> ls) {
        this.ls = ls;
    }

    public Experience getCurrentExperience() {
        return currentExperience;
    }

    public void setCurrentExperience(Experience currenExperience) {
        this.currentExperience = currenExperience;
    }

   
}
