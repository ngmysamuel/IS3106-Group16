/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Category;
import entity.Language;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.LanguageControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "languageManagementManagedBean")
@ViewScoped
public class LanguageManagementManagedBean implements Serializable{

    @EJB(name = "LanguageControllerLocal")
    private LanguageControllerLocal languageControllerLocal;

    private List<Language> languages;
    
    private List<Language> filteredLanguages;
    
    private Language newLanguage;
    
    private Language selectedLanguageToView;
    
    private Language selectedLanguageToUpdate;
    
    private Language selectedLanguageToDelete;
       
    public LanguageManagementManagedBean() {
        newLanguage = new Language();
    }
    
    @PostConstruct
    public void PostConstruct(){
        languages = languageControllerLocal.retrieveAllLanguages();
    }
    
    public void viewLanguageDetails(ActionEvent event) throws IOException{
        
        Long languageIdToView = (Long) event.getComponent().getAttributes().get("languageId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("languageIdToView",languageIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewLanguageDetails.xhtml");
        
    }
    
    public void createNewLanguage(){
        try{
            
            Language lg = languageControllerLocal.createNewLanguage(newLanguage);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Language created successfully (Language ID: " + lg.getLanguageId()+ ")", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Language: " + ex.getMessage(), null));
        
        }
    }
    
    public void doUpdateLanguage(ActionEvent event){
        selectedLanguageToUpdate = (Language) event.getComponent().getAttributes().get("languageToUpdate");
    }
    
    public void updateLanguage(ActionEvent event){
        try{
            languageControllerLocal.updateLanguage(selectedLanguageToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Language Updated Successfully", null));
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while updating language: " + ex.getMessage(), null));
        }
    }
    
    public void deleteLanguage(ActionEvent event){
        try{
            selectedLanguageToDelete = (Language) event.getComponent().getAttributes().get("languageToDelete");
            
            languageControllerLocal.deleteLanguage(selectedLanguageToDelete.getLanguageId());
            
            languages.remove(selectedLanguageToDelete);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Language deleted successfully", null));
            
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting language: " + ex.getMessage(), null));
        }
        
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public List<Language> getFilteredLanguages() {
        return filteredLanguages;
    }

    public void setFilteredLanguages(List<Language> filteredLanguages) {
        this.filteredLanguages = filteredLanguages;
    }

    public Language getNewLanguage() {
        return newLanguage;
    }

    public void setNewLanguage(Language newLanguage) {
        this.newLanguage = newLanguage;
    }

    public Language getSelectedLanguageToView() {
        return selectedLanguageToView;
    }

    public void setSelectedLanguageToView(Language selectedLanguageToView) {
        this.selectedLanguageToView = selectedLanguageToView;
    }

    public Language getSelectedLanguageToUpdate() {
        return selectedLanguageToUpdate;
    }

    public void setSelectedLanguageToUpdate(Language selectedLanguageToUpdate) {
        this.selectedLanguageToUpdate = selectedLanguageToUpdate;
    }

    public Language getSelectedLanguageToDelete() {
        return selectedLanguageToDelete;
    }

    public void setSelectedLanguageToDelete(Language selectedLanguageToDelete) {
        this.selectedLanguageToDelete = selectedLanguageToDelete;
    }
    
    
    
}
