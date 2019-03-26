/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.ExperienceDate;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.ExperienceDateControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "updateExperienceDateManagedBean")
@ViewScoped
public class UpdateExperienceDateManagedBean implements Serializable {

    @EJB(name = "ExperienceDateControllerLocal")
    private ExperienceDateControllerLocal experienceDateControllerLocal;

    private Long experienceDateIdToUpdate; 
    private ExperienceDate experienceDateToUpdate; 
    
    public UpdateExperienceDateManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        
        experienceDateIdToUpdate = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("experienceDateIdToUpdate");
        
        try{
            
            experienceDateToUpdate = experienceDateControllerLocal.retrieveExperienceDateByDateId(experienceDateIdToUpdate);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
            
        }
        
        
    }
    
    public void back(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceDateIdToView", experienceDateIdToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDateDetails.xhtml");
    }
    
    public void foo(){
        
    }
    
    public void updateExperienceDate(){
        try{
            
            experienceDateControllerLocal.updateExperienceDate(experienceDateToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Experience Date updated successfully", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error occurred while updating Experience Date" + ex.getMessage(), null));
        
        }
    }

    public Long getExperienceDateIdToUpdate() {
        return experienceDateIdToUpdate;
    }

    public void setExperienceDateIdToUpdate(Long experienceDateIdToUpdate) {
        this.experienceDateIdToUpdate = experienceDateIdToUpdate;
    }

    public ExperienceDate getExperienceDateToUpdate() {
        return experienceDateToUpdate;
    }

    public void setExperienceDateToUpdate(ExperienceDate experienceDateToUpdate) {
        this.experienceDateToUpdate = experienceDateToUpdate;
    }
    
    
}
