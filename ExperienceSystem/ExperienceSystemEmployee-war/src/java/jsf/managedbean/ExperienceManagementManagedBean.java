/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Experience;
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
@Named(value = "experienceManagementManagedBean")
@ViewScoped
public class ExperienceManagementManagedBean implements Serializable {

    @EJB(name = "ExperienceControllerLocal")
    private ExperienceControllerLocal experienceControllerLocal;
 
    private List<Experience> experiences;
    private List<Experience> filteredExperiences;

    public ExperienceManagementManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        experiences = experienceControllerLocal.retrieveAllExperiences();
    }
    
    public void viewExperienceDetails(ActionEvent event) throws IOException{
        Experience experienceToView = (Experience)event.getComponent().getAttributes().get("experienceToView");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceIdToView", experienceToView.getExperienceId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDetails.xhtml");
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public List<Experience> getFilteredExperiences() {
        return filteredExperiences;
    }

    public void setFilteredExperiences(List<Experience> filteredExperiences) {
        this.filteredExperiences = filteredExperiences;
    }
    
}
