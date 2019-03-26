/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Language;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.LanguageControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "updateLanguageManagedBean")
@ViewScoped
public class UpdateLanguageManagedBean implements Serializable {

    @EJB(name = "LanguageControllerLocal")
    private LanguageControllerLocal languageControllerLocal;

    private Long languageIdToUpdate;
    private Language languageToUpdate; 
    
    public UpdateLanguageManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        
        languageIdToUpdate = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("languageIdToUpdate");
        
        try{
            
            languageToUpdate = languageControllerLocal.retrieveLanguageById(languageIdToUpdate);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the language details: " + ex.getMessage(), null));
        
        }
    }
    
    public void foo(){
        
    }
    
    public void updateLanguage(){
        try{
            
            languageControllerLocal.updateLanguage(languageToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Language Updated Successfully", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while updating language: " + ex.getMessage(), null));
        
        }
    }

    public Long getLanguageIdToUpdate() {
        return languageIdToUpdate;
    }

    public void setLanguageIdToUpdate(Long languageIdToUpdate) {
        this.languageIdToUpdate = languageIdToUpdate;
    }

    public Language getLanguageToUpdate() {
        return languageToUpdate;
    }

    public void setLanguageToUpdate(Language languageToUpdate) {
        this.languageToUpdate = languageToUpdate;
    }          

}
