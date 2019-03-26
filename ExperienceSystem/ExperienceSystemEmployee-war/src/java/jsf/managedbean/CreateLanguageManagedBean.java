/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Language;
import entity.Location;
import java.io.Serializable;
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
@Named(value = "createLanguageManagedBean")
@ViewScoped
public class CreateLanguageManagedBean implements Serializable {

    @EJB(name = "LanguageControllerLocal")
    private LanguageControllerLocal languageControllerLocal;
    
    private Language newLanguage;
    private Long languageId;
    
    public CreateLanguageManagedBean() {
    }
    
    public void createNewLoction(){
        try{
            
            Language lg = languageControllerLocal.createNewLanguage(newLanguage);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Language created successfully (Language ID: " + lg.getLanguageId()+ ")", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Language: " + ex.getMessage(), null));
        
        }
    }

    public Language getNewLanguage() {
        return newLanguage;
    }

    public void setNewLanguage(Language newLanguage) {
        this.newLanguage = newLanguage;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }
    
    
}
