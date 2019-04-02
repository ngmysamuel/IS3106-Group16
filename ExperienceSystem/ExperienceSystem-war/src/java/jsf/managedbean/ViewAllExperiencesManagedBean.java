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
import javax.annotation.PreDestroy;
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
    
    private List<Experience> allExperiences;
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
    
    private Date today;

    public ViewAllExperiencesManagedBean() {
        numOfPeople = 1;
        filteredExperiences = new ArrayList();
    }
    
    @PostConstruct
    public void postConstruct(){
        System.out.println("******** ViewAllExperiencesManagedBean: postConstruct()");
        allExperiences = experienceControllerLocal.retrieveAllExperiences();
        categories = categoryControllerLocal.retrieveAllCategories();
        types = typeControllerLocal.retrieveAllTypes();
        locations = locationControllerLocal.retrieveAllLocations();
        languages = languageControllerLocal.retrieveAllLanguages();
        
        if(FacesContext.getCurrentInstance().getExternalContext().getFlash().containsKey("searchDate")){
            selectedDate = (Date)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("searchDate");
            selectedCategoryId = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("searchCategoryId");
            numOfPeople = (Integer)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("searchNumOfPeople");
            System.out.println("    **** selectedDate: " + selectedDate.toString());
            System.out.println("    **** selectedCategoryId: " + selectedCategoryId);
            System.out.println("    **** numOfPeople: " + numOfPeople);
        }
         
        filterExperience();
    }
    
    @PreDestroy
    public void preDestroy(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("searchDate");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("searchNumOfPeople");
    }
    
    public void filterExperience(){
        System.out.println("******** ViewAllExperiencesManagedBean: filterExperience()");

        filteredExperiences.clear();
        filteredExperiences.addAll(allExperiences);
        
        // filter experiences property by property
        // the sequence is very important because the experience dates available would be filtered based on the previous results
        
        // Date
        if(selectedDate != null) {
            filteredExperiences = experienceControllerLocal.filterExperienceByDate(filteredExperiences, selectedDate);
        }
        // Number of people
        filteredExperiences = experienceControllerLocal.filterExperienceBySlotsAvailable(filteredExperiences, numOfPeople);
        // Category
        if(selectedCategoryId != null) {
            filteredExperiences = experienceControllerLocal.filterExperienceByCategory(filteredExperiences, selectedCategoryId);
        }
//        if(searchString != null && searchString.trim().length() > 0){
//            allExperiences = experienceController.retrieveExperienceByName(searchString);
//        } else {allExperiences = experienceController.retrieveAllExperiences();}
//        

    }
    
    public void filterExperience(ActionEvent event) {
        filterExperience();
    }
    
    public void viewExperienceDetails(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("experienceEntityToView", selectedExperienceToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewExperienceDetails.xhtml");
    }
    
    
    public Date minDate(List<Date> dates){
        Date min = dates.get(0);
        for(Date d: dates){
            if(min.compareTo(d) > 0){
                min = d;
            }
        }
        return min;
    }
    
    public Date maxDate(List<Date> dates){
        Date max = dates.get(0);
        for(Date d: dates){
            if(max.compareTo(d) < 0){
                max = d;
            }
        }
        return max;
    }

    public Experience getSelectedExperienceToView() {
        return selectedExperienceToView;
    }

    public void setSelectedExperienceToView(Experience selectedExperienceToView) {
        this.selectedExperienceToView = selectedExperienceToView;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("experienceEntityToView", selectedExperienceToView);
    }

    public List<Experience> getAllExperiences() {
        return allExperiences;
    }

    public void setAllExperiences(List<Experience> allExperiences) {
        this.allExperiences = allExperiences;
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

    public Date getToday() {
        return new Date();
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public List<Experience> getFilteredExperiences() {
        return filteredExperiences;
    }

    public void setFilteredExperiences(List<Experience> filteredExperiences) {
        this.filteredExperiences = filteredExperiences;
    }

}