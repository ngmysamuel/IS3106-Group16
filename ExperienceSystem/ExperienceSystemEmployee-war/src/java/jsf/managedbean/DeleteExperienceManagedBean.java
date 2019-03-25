
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
import util.exception.ExperienceNotFoundException;

/**
 *
 * @author zhangruichun
 */
@Named(value = "deleteExperienceManagedBean")
@ViewScoped
public class DeleteExperienceManagedBean {

    @EJB(name = "ExperienceControllerLocal")
    private ExperienceControllerLocal experienceControllerLocal;
    
    private Long experienceIdToDelete;
    private Experience experienceToDelete;
    private String deleteExperienceReason; 
    
    public DeleteExperienceManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        
        experienceIdToDelete = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("experienceIdToDelete");
        deleteExperienceReason = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("deleteExperienceReason");
        
        try{
            
            experienceToDelete = experienceControllerLocal.retrieveExperienceById(experienceIdToDelete);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred" + ex.getMessage(), null));
        
        }
    }
    
    public void deleteExperience(){
        try{
            
            experienceControllerLocal.deleteExperience(experienceIdToDelete, deleteExperienceReason);
            
            experienceIdToDelete = null;
            experienceToDelete = null;
            deleteExperienceReason = null;
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred" + ex.getMessage(), null));
        
        }
    }
    
    public void back(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceIdToView", experienceIdToDelete);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDetails.xhtml");
    }

    public void foo(){
    }
    
    public Long getExperienceIdToDelete() {
        return experienceIdToDelete;
    }

    public void setExperienceIdToDelete(Long experienceIdToDelete) {
        this.experienceIdToDelete = experienceIdToDelete;
    }

    public Experience getExperienceToDelete() {
        return experienceToDelete;
    }

    public void setExperienceToDelete(Experience experienceToDelete) {
        this.experienceToDelete = experienceToDelete;
    }

    public String getDeleteExperienceReason() {
        return deleteExperienceReason;
    }

    public void setDeleteExperienceReason(String deleteExperienceReason) {
        this.deleteExperienceReason = deleteExperienceReason;
    }
    
    
}
