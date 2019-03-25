/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Language;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.LanguageControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewAllLanguagesManagedBean")
@RequestScoped
public class ViewAllLanguagesManagedBean {

    @EJB(name = "LanguageControllerLocal")
    private LanguageControllerLocal languageControllerLocal;

    private List<Language> languages;
    
    public ViewAllLanguagesManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        
        languages = languageControllerLocal.retrieveAllLanguages();
        
    }
    
    public void updateLanguage(ActionEvent event) throws IOException{
        Long languageToUpdate = (Long) event.getComponent().getAttributes().get("languageId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("languageToUpdate", languageToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateLanguage.xhtml");
    }
    
    public void deleteLanguage(ActionEvent event) throws IOException{
        Long languageToDelete = (Long) event.getComponent().getAttributes().get("languageId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("languageToDelete", languageToDelete);
        FacesContext.getCurrentInstance().getExternalContext().redirect("deleteLanguage.xhtml");
    }
    
    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }
    
    
}
