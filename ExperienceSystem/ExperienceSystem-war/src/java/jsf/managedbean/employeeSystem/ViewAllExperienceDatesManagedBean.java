/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Experience;
import entity.ExperienceDate;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewAllExperienceDatesManagedBean")
@RequestScoped
public class ViewAllExperienceDatesManagedBean {

    @EJB(name = "ExperienceDateControllerLocal")
    private ExperienceDateControllerLocal experienceDateControllerLocal;

    @EJB(name = "ExperienceControllerLocal")
    private ExperienceControllerLocal experienceControllerLocal;

    private Experience experience;
    private Long experienceIdToView; 
    private List<ExperienceDate> experienceDates;
    
            
    public ViewAllExperienceDatesManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        
        experienceIdToView = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("experienceIdToView");
        
        try{
            if(experienceIdToView != null){
            experience = experienceControllerLocal.retrieveExperienceById(experienceIdToView);
            
            experienceDates = experience.getExperienceDates();            
            }
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred" + ex.getMessage(), null));
        
        }
    }
    
    public void experienceDateDetails(ActionEvent event) throws IOException{
        
        Long experienceDateIdToView = (Long) event.getComponent().getAttributes().get("experienceDateId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceDateIdToView", experienceDateIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDateDetails.xhtml");
        
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public Long getExperienceIdToView() {
        return experienceIdToView;
    }

    public void setExperienceIdToView(Long experienceIdToView) {
        this.experienceIdToView = experienceIdToView;
    }

    public List<ExperienceDate> getExperienceDates() {
        return experienceDates;
    }

    public void setExperienceDates(List<ExperienceDate> experienceDates) {
        this.experienceDates = experienceDates;
    }
    
    
    
    
}
