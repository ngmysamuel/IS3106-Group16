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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
@Named(value = "experienceManagementManagedBean")
@ViewScoped
public class ExperienceManagementManagedBean implements Serializable{

    @EJB(name = "CategoryControllerLocal")
    private CategoryControllerLocal categoryControllerLocal;

    @EJB(name = "TypeControllerLocal")
    private TypeControllerLocal typeControllerLocal;

    @EJB(name = "LocationControllerLocal")
    private LocationControllerLocal locationControllerLocal;

    @EJB(name = "LanguageControllerLocal")
    private LanguageControllerLocal languageControllerLocal;

    @EJB(name = "ExperienceControllerLocal")
    private ExperienceControllerLocal experienceControllerLocal;
    
    
    private List<Experience> experiences;
    private List<Experience> filteredExperiences;
    
    private List<Location> locations;
    private List<Category> categories;
    private List<Language> languages;
    private List<Type> types;
    
    private Experience selectedExperienceToView;
    private Experience selectedExperienceToUpdate;
    private Experience selectedExperienceToDelete;
    
    private Location locationToUpdate;
    private Long locationIdToUpdate;
    
    private Language languageToUpdate;
    private Long languageIdToUpdate;
    
    private Category categoryToUpdate;
    private Long categoryIdToUpdate;
    
    private Type typeToUpdate;
    private Long typeIdToUpdate;
    
    private Date date;
    private int capacity;
    private BigDecimal price; 
    
    public ExperienceManagementManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        experiences = experienceControllerLocal.retrieveAllExperiences();
        locations = locationControllerLocal.retrieveAllLocations();
        languages = languageControllerLocal.retrieveAllLanguages();
        categories = categoryControllerLocal.retrieveAllCategories();
        types = typeControllerLocal.retrieveAllTypes();
    }
    
    public void viewExperienceDetails(ActionEvent event) throws IOException{
        Long experienceIdToView = (Long) event.getComponent().getAttributes().get("experienceId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceIdToView", experienceIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDetails.xhtml");
    }
    
    public void doUpdateProduct(ActionEvent event){
        selectedExperienceToUpdate = (Experience) event.getComponent().getAttributes().get("experienceIdToUpdate");
        
        categoryToUpdate = selectedExperienceToUpdate.getCategory();
        locationToUpdate = selectedExperienceToUpdate.getLocation();
        languageToUpdate = selectedExperienceToUpdate.getLanguage();
        typeToUpdate = selectedExperienceToUpdate.getType();
    }
}
