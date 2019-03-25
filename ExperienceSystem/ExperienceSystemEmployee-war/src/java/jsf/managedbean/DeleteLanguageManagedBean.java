/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Language;
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
@Named(value = "deleteLanguageManagedBean")
@ViewScoped
public class DeleteLanguageManagedBean {

    @EJB(name = "LanguageControllerLocal")
    private LanguageControllerLocal languageControllerLocal;

    private Long languageIdToDelete;
    private Language languageToDelete;
    
    public DeleteLanguageManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        languageIdToDelete = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("languageIdToDelete");
        
        try{
            
            languageToDelete = languageControllerLocal.retrieveLanguageById(languageIdToDelete);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the language details: " + ex.getMessage(), null));
        
        }
        
    }
    
    public void deleteLanguage(){
        try{
            
            languageControllerLocal.deleteLanguage(languageIdToDelete);
            
            languageIdToDelete = null;
            languageIdToDelete = null;
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting language: " + ex.getMessage(), null));
            
        }
        
    }

    public Long getLanguageIdToDelete() {
        return languageIdToDelete;
    }

    public void setLanguageIdToDelete(Long languageIdToDelete) {
        this.languageIdToDelete = languageIdToDelete;
    }

    public Language getLanguageToDelete() {
        return languageToDelete;
    }

    public void setLanguageToDelete(Language languageToDelete) {
        this.languageToDelete = languageToDelete;
    }
    
    
}
