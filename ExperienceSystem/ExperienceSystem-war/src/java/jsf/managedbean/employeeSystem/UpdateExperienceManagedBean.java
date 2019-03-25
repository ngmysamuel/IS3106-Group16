/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Category;
import entity.Experience;
import entity.Language;
import entity.Location;
import entity.Type;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.CategoryControllerLocal;
import stateless.ExperienceControllerLocal;
import stateless.LanguageControllerLocal;
import stateless.LocationControllerLocal;
import stateless.TypeControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "updateExperienceManagedBean")
@ViewScoped
public class UpdateExperienceManagedBean {

    @EJB(name = "ExperienceControllerLocal")
    private ExperienceControllerLocal experienceControllerLocal;
    
    @EJB(name = "TypeControllerLocal")
    private TypeControllerLocal typeControllerLocal;

    @EJB(name = "LocationControllerLocal")
    private LocationControllerLocal locationControllerLocal;

    @EJB(name = "LanguageControllerLocal")
    private LanguageControllerLocal languageControllerLocal;

    @EJB(name = "CategoryControllerLocal")
    private CategoryControllerLocal categoryControllerLocal;

    private Long experienceIdToUpdate; 
    private Experience experienceToUpdate;
    
    private Long categoryId;
    private Long languageId;
    private Long locationId;
    private Long typeId;
    
    private List<Category> categoryEntities;
    private List<Language> languageEntities;
    private List<Location> locationEntities;
    private List<Type> typeEntities;
    
            
           
    public UpdateExperienceManagedBean() {
    }
    
    
    @PostConstruct 
    public void postConstruct(){
        
        experienceIdToUpdate = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("experienceIdToUpdate");
        
        try{
            
            experienceToUpdate = experienceControllerLocal.retrieveExperienceById(experienceIdToUpdate);
            
            categoryId = experienceToUpdate.getCategory().getCategoryId();
            languageId = experienceToUpdate.getLanguage().getLanguageId();
            locationId = experienceToUpdate.getLocation().getLocationId();
            typeId = experienceToUpdate.getType().getTypeId();
            
            categoryEntities = categoryControllerLocal.retrieveAllCategories();
            languageEntities = languageControllerLocal.retrieveAllLanguages();
            locationEntities = locationControllerLocal.retrieveAllLocations();
            typeEntities = typeControllerLocal.retrieveAllTypes();
                       
                    
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
            
        }
    }
    
    public void back(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceIdToUpdate", experienceIdToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDetails.xhtml");
    }
    
    public void foo(){
        
    }
    
    public void updateProduct(){
        
        if(categoryId == 0){
            categoryId = null;
        }
        
        if(locationId == 0){
            locationId = null;
        }
        
        if(languageId == 0){
            languageId = null;
        }
        
        if(typeId == 0){
            typeId = null;
        }
        
        try{
            
            experienceControllerLocal.updateExperienceInformation(experienceToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Experience updated successfully", null));
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred during updating: " + ex.getMessage(), null));
        
        }
                
    }

    public Long getExperienceIdToUpdate() {
        return experienceIdToUpdate;
    }

    public void setExperienceIdToUpdate(Long experienceIdToUpdate) {
        this.experienceIdToUpdate = experienceIdToUpdate;
    }

    public Experience getExperienceToUpdate() {
        return experienceToUpdate;
    }

    public void setExperienceToUpdate(Experience experienceToUpdate) {
        this.experienceToUpdate = experienceToUpdate;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public List<Category> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<Category> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public List<Language> getLanguageEntities() {
        return languageEntities;
    }

    public void setLanguageEntities(List<Language> languageEntities) {
        this.languageEntities = languageEntities;
    }

    public List<Location> getLocationEntities() {
        return locationEntities;
    }

    public void setLocationEntities(List<Location> locationEntities) {
        this.locationEntities = locationEntities;
    }

    public List<Type> getTypeEntities() {
        return typeEntities;
    }

    public void setTypeEntities(List<Type> typeEntities) {
        this.typeEntities = typeEntities;
    }
    
    
    
}
