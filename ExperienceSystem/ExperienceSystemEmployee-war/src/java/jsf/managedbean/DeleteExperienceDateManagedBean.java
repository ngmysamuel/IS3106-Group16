/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.ExperienceDate;
import java.io.IOException;
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
@Named(value = "deleteExperienceDateManagedBean")
@ViewScoped
public class DeleteExperienceDateManagedBean {

    @EJB(name = "ExperienceDateControllerLocal")
    private ExperienceDateControllerLocal experienceDateControllerLocal;
    
    private Long experienceDateIdToDelete;
    private ExperienceDate experienceDateToDelete;
    private String experienceDateDeleteReason; 
    
    public DeleteExperienceDateManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        
        experienceDateIdToDelete = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("experienceDateIdToDelete");
        experienceDateDeleteReason = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("experienceDateDeleteReason");
        
        try{
            
            experienceDateToDelete = experienceDateControllerLocal.retrieveExperienceDateByDateId(experienceDateIdToDelete);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while delete experience date:" +ex.getMessage(), null));
            
        }
    }
    
    public void deleteExperienceDate(){
        try{
            
            experienceDateControllerLocal.deleteExperienceDate(experienceDateIdToDelete, experienceDateDeleteReason);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Experience Date Delete Successfully", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while deleting experience date: "+ ex.getMessage(), null));
        
        }
    }
    
    public void back(ActionEvent event) throws IOException{
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceDateIdToView", experienceDateIdToDelete);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDateDetails.xhtml");
        
    }
    
    public void foo(){
        
    }

    public Long getExperienceDateIdToDelete() {
        return experienceDateIdToDelete;
    }

    public void setExperienceDateIdToDelete(Long experienceDateIdToDelete) {
        this.experienceDateIdToDelete = experienceDateIdToDelete;
    }

    public ExperienceDate getExperienceDateToDelete() {
        return experienceDateToDelete;
    }

    public void setExperienceDateToDelete(ExperienceDate experienceDateToDelete) {
        this.experienceDateToDelete = experienceDateToDelete;
    }

    public String getExperienceDateDeleteReason() {
        return experienceDateDeleteReason;
    }

    public void setExperienceDateDeleteReason(String experienceDateDeleteReason) {
        this.experienceDateDeleteReason = experienceDateDeleteReason;
    }
    
    
}
