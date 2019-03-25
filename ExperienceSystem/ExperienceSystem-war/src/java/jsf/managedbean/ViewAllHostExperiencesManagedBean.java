/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Experience;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "viewAllHostExperiencesManagedBean")
@ViewScoped
public class ViewAllHostExperiencesManagedBean implements Serializable {

    private List<Experience> hostExperiences;
    private Experience experienceToView;
    
    public ViewAllHostExperiencesManagedBean() {
        // generate dummy Experiences
        hostExperiences = new ArrayList();
        hostExperiences.add(new Experience("Experience one", "Desc1"));
        hostExperiences.add(new Experience("Experience one", "Desc1"));
        hostExperiences.add(new Experience("Experience one", "Desc1"));
        hostExperiences.add(new Experience("Experience one", "Desc1"));
        hostExperiences.add(new Experience("Experience one", "Desc1"));
    }

    public List<Experience> getHostExperiences() {
        return hostExperiences;
    }

    public void setHostExperiences(List<Experience> hostExperiences) {
        this.hostExperiences = hostExperiences;
    }

    public Experience getExperienceToView() {
        return experienceToView;
    }

    public void setExperienceToView(Experience experienceToView) {
        this.experienceToView = experienceToView;
    }
    
    
}
