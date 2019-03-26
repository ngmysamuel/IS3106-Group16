/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Experience;
import java.io.IOException;
import java.io.Serializable;
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
@Named(value = "searchExperienceByIdManagedBean")
@ViewScoped
public class SearchExperienceByIdManagedBean implements Serializable {

    @EJB(name = "ExperienceControllerLocal")
    private ExperienceControllerLocal experienceControllerLocal;
    
    private Long searchId;
    private Experience experience;
    /**
     * Creates a new instance of SearchExperienceByIdManagedBean
     */
    public SearchExperienceByIdManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        searchId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("experienceSearchById");
        try{
            
            if(searchId != null){
                
                experience = experienceControllerLocal.retrieveExperienceById(searchId);
                
            }
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred" + ex.getMessage(), null));
        
        }
        
    }
    
    public void searchExperience(){
        searchId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("experienceSearchById");
        try{
            
            if(searchId != null){
                
                experience = experienceControllerLocal.retrieveExperienceById(searchId);
                
            }
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred" + ex.getMessage(), null));
        
        }
        
    }
    
    public void viewExperienceDetails(ActionEvent event) throws IOException{
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceIdToView", searchId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("backMode", "searchExperienceById");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDetails.xhtml");
        
    }

    public Long getSearchId() {
        return searchId;
    }

    public void setSearchId(Long searchId) {
        this.searchId = searchId;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }
    
    
}
