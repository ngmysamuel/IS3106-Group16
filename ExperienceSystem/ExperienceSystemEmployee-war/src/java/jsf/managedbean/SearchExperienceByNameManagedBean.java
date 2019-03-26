
package jsf.managedbean;

import entity.Experience;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import stateless.ExperienceControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "searchExperienceByNameManagedBean")
@ViewScoped
public class SearchExperienceByNameManagedBean implements Serializable {

    @EJB
    private ExperienceControllerLocal experienceControllerLocal;
    
    private String searchName;
    private Experience experience; 
    
    public SearchExperienceByNameManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        searchName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("experienceSearchByName");
        try{
            
            if(searchName != null || searchName.trim().length() != 0){
                
                experience = experienceControllerLocal.retrieveExperienceByTitle(searchName);
                
            }
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred" + ex.getMessage(), null));
        
        }
        
    }
    
    public void searchExperience(){
        searchName = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("experienceSearchByName");
        try{
            
            if(searchName != null || searchName.trim().length() != 0){
                
                experience = experienceControllerLocal.retrieveExperienceByTitle(searchName);
                
            }
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred" + ex.getMessage(), null));
        
        }
        
    }
    
    public void viewExperienceDetails(ActionEvent event) throws IOException{
        Long experienceIdToView = (Long)event.getComponent().getAttributes().get("experienceId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceIdToView", experienceIdToView);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("backMode", "searchExperienceByName");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDetails.xhtml");
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }
   
}
