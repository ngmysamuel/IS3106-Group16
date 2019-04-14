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
import java.util.ArrayList;
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
 * @author Asus
 */
@Named(value = "viewAllExperiencesManagedBean")
@ViewScoped
public class ViewAllExperiencesManagedBean implements Serializable{

    @EJB
    private LocationControllerLocal locationControllerLocal;
    @EJB
    private TypeControllerLocal typeControllerLocal;
    @EJB
    private CategoryControllerLocal categoryControllerLocal;
    @EJB
    private LanguageControllerLocal languageControllerLocal;
    @EJB
    private ExperienceControllerLocal experienceControllerLocal;
    
    private List<Experience> filteredExperiences;
    private List<Type> types;
    private List<Category> categories;
    private List<Location> locations;
    private List<Language> languages;
    
    private Long selectedCategoryId;
    private Long selectedTypeId;
    private Long selectedLocationId;
    private Long selectedLanguageId;
    private Date selectedDate;
    private Experience selectedExperienceToView;
    private Integer numOfPeople;
    
    private String searchString;

    public ViewAllExperiencesManagedBean() {
        numOfPeople = 1;
        filteredExperiences = new ArrayList();
    }
    
    @PostConstruct
    public void postConstruct(){
        System.out.println("******** ViewAllExperiencesManagedBean: postConstruct()");
        System.out.println("-----------------------------");
        categories = categoryControllerLocal.retrieveAllCategories();
        types = typeControllerLocal.retrieveAllTypes();
        locations = locationControllerLocal.retrieveAllLocations();
        languages = languageControllerLocal.retrieveAllLanguages();
        
        // Flash scope attributes come from index.xhtml
        if(FacesContext.getCurrentInstance().getExternalContext().getFlash().containsKey("searchDate")){
            selectedDate = (Date)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("searchDate");
            selectedCategoryId = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("searchCategoryId");
            numOfPeople = (Integer)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("searchNumOfPeople");
        } else {
            selectedDate = (Date)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchDate");
            selectedCategoryId = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchCategoryId");
            selectedTypeId = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchTypeId");
            selectedLocationId = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchLocationId");
            selectedLanguageId = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchLanguageId");
            numOfPeople = (Integer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchNumOfPeople");
        }
         
        filterExperience();
    }
    
    public void filterExperience(){
        System.out.println("******** ViewAllExperiencesManagedBean: filterExperience()");

        filteredExperiences.clear();
        filteredExperiences.addAll(experienceControllerLocal.retrieveAllExperiences());
        
        // filter experiences property by property
        // the sequence is very important because the experience dates available would be filtered based on the previous results
        
        // Experience must be active
        filteredExperiences = experienceControllerLocal.filterExperienceByActiveState(filteredExperiences);
        
        // Date
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("searchDate", selectedDate);
        if(selectedDate != null) {
            filteredExperiences = experienceControllerLocal.filterExperienceByDate(filteredExperiences, selectedDate);
        }
        // Number of people
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("searchNumOfPeople", numOfPeople);
        if(numOfPeople != null) {
            filteredExperiences = experienceControllerLocal.filterExperienceBySlotsAvailable(filteredExperiences, numOfPeople);
        } else {
            filteredExperiences = experienceControllerLocal.filterExperienceBySlotsAvailable(filteredExperiences, 1);
        }
        // Category
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("searchCategoryId", selectedCategoryId);
        if(selectedCategoryId != null && selectedCategoryId.compareTo(new Long(0)) != 0) {
            filteredExperiences = experienceControllerLocal.filterExperienceByCategory(filteredExperiences, selectedCategoryId);
        }
        // Type
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("searchTypeId", selectedTypeId);
        if(selectedTypeId != null && selectedTypeId.compareTo(new Long(0)) != 0) {
            filteredExperiences = experienceControllerLocal.filterExperienceByType(filteredExperiences, selectedTypeId);
        }
        // Location
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("searchLocationId", selectedLocationId);
        if(selectedLocationId != null && selectedLocationId.compareTo(new Long(0)) != 0) {
            filteredExperiences = experienceControllerLocal.filterExperienceByLocation(filteredExperiences, selectedLocationId);
        }
        // Language
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("searchLanguageId", selectedLanguageId);
        if(selectedLanguageId != null && selectedLanguageId.compareTo(new Long(0)) != 0) {
            filteredExperiences = experienceControllerLocal.filterExperienceByLanguage(filteredExperiences, selectedLanguageId);
        }
//        if(searchString != null && searchString.trim().length() > 0){
//            allExperiences = experienceController.retrieveExperienceByName(searchString);
//        } else {allExperiences = experienceController.retrieveAllExperiences();}   
        System.out.println("**** SUMMARY: remaining experience size: " + filteredExperiences.size());
        System.out.println("-----------------------------");

    }
    
    public void filterExperience(ActionEvent event) {
        filterExperience();
    }
    
    public void viewExperienceDetails(ActionEvent event) throws IOException{
        System.out.println("******** ViewAllExperiencesManagedBean: viewExperienceDetails() ");
        filteredExperiences.clear();
        Experience experienceToView = (Experience)event.getComponent().getAttributes().get("experienceToView");     
        System.out.println("**** experienceToView: " + experienceToView.getTitle());
        System.out.println("**** experience date listing:  " + experienceToView.getExperienceDates().size());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("experienceToView", experienceToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDetails.xhtml");
    }

    public Experience getSelectedExperienceToView() {
        return selectedExperienceToView;
    }

    public void setSelectedExperienceToView(Experience selectedExperienceToView) {
        System.out.println("******** ViewAllExperiencesManagedBean: setSelectedExperienceToView() ");  
        System.out.println("**** selectedExperienceToView: " + selectedExperienceToView.getTitle());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("experienceToView", selectedExperienceToView);
        this.selectedExperienceToView = selectedExperienceToView; 
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Long getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(Long selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public Long getSelectedTypeId() {
        return selectedTypeId;
    }

    public void setSelectedTypeId(Long selectedTypeId) {
        this.selectedTypeId = selectedTypeId;
    }

    public Long getSelectedLocationId() {
        return selectedLocationId;
    }

    public void setSelectedLocationId(Long selectedLocationId) {
        this.selectedLocationId = selectedLocationId;
    }

    public Long getSelectedLanguageId() {
        return selectedLanguageId;
    }

    public void setSelectedLanguageId(Long selectedLanguageId) {
        this.selectedLanguageId = selectedLanguageId;
    }

    public Integer getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(Integer numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public List<Experience> getFilteredExperiences() {
        return filteredExperiences;
    }

    public void setFilteredExperiences(List<Experience> filteredExperiences) {
        this.filteredExperiences = filteredExperiences;
    }

}