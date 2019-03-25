/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Experience;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.ExperienceControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewAllExperiencesManagedBean")
@RequestScoped
public class ViewAllExperiencesManagedBean {

    @EJB(name = "ExperienceControllerLocal")
    private ExperienceControllerLocal experienceControllerLocal;
    
    private List<Experience> experiences; 
    
    public ViewAllExperiencesManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        experiences = experienceControllerLocal.retrieveAllExperiences();
    }
    
    public void viewExperienceDetails(ActionEvent event) throws IOException{
        Long experienceIdToView = (Long) event.getComponent().getAttributes().get("experienceId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceIdToView", experienceIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }
    
    
    
    
}
