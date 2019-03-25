/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Experience;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.ExperienceControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewExperienceDetailsManagedBean")
@ViewScoped
public class ViewExperienceDetailsManagedBean {

    @EJB(name = "ExperienceControllerLocal")
    private ExperienceControllerLocal experienceControllerLocal;

    private Long experienceIdToView;
    private String backMode;
    private Experience experienceToView; 
    
    public ViewExperienceDetailsManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        
        experienceIdToView = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("experienceIdToView");
        backMode = (String)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("backMode");
        
        try{
            
            experienceToView = experienceControllerLocal.retrieveExperienceById(experienceIdToView);
            
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the experience details: " + ex.getMessage(), null));        
        }
    }
    
    public void back(ActionEvent event) throws IOException{
        if(backMode == null || backMode.trim().length() == 0){
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllExperiences.xhtml");
        }else{
            FacesContext.getCurrentInstance().getExternalContext().redirect( backMode + ".xhtml");
        }
    }
    
    
    public void foo(){
        
    }
    
    public void updateExperience(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceIdToUpdate", experienceIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateExperience.xhtml");
    }
    
    public void deleteExperience(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceIdToDelete", experienceIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("deleteExperience.xhtml");
    }

    public Long getExperienceIdToView() {
        return experienceIdToView;
    }

    public void setExperienceIdToView(Long experienceIdToView) {
        this.experienceIdToView = experienceIdToView;
    }

    public String getBackMode() {
        return backMode;
    }

    public void setBackMode(String backMode) {
        this.backMode = backMode;
    }

    public Experience getExperienceToView() {
        return experienceToView;
    }

    public void setExperienceToView(Experience experienceToView) {
        this.experienceToView = experienceToView;
    }

    
        
          
    
    
         
}
