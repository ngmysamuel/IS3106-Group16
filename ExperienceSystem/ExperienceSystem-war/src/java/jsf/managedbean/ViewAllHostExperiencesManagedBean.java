/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Experience;
import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.ExperienceControllerLocal;

@Named(value = "viewAllHostExperiencesManagedBean")
@ViewScoped
public class ViewAllHostExperiencesManagedBean implements Serializable {

    @EJB
    private ExperienceControllerLocal experienceControllerLocal;
    
    private List<Experience> hostExperiences;
    private User currentUser;
    
    public ViewAllHostExperiencesManagedBean() {
        
    }
    
    @PostConstruct
    public void postConstruct() {
        System.out.println("******** ViewAllHostExperiencesManagedBean: postConstruct()");
        currentUser = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        hostExperiences = experienceControllerLocal.retrieveAllHostExperienceByHostId(currentUser.getUserId());
        System.out.println("    **** hostExperiences.size: " + hostExperiences.size());
    }
    
    public void viewHostExperienceDetails(ActionEvent event) throws IOException {
        Experience hostExperienceToView = (Experience)event.getComponent().getAttributes().get("hostExperienceToView");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("hostExperienceIdToView", hostExperienceToView.getExperienceId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("manageHostExperience.xhtml");
    }

    public List<Experience> getHostExperiences() {
        return hostExperiences;
    }

    public void setHostExperiences(List<Experience> hostExperiences) {
        this.hostExperiences = hostExperiences;
    }
    
}
