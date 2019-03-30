/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Category;
import entity.Experience;
import entity.Location;
import entity.Type;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.bean.ViewScoped;
import stateless.CategoryControllerLocal;
import stateless.ExperienceControllerLocal;
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
    private LocationControllerLocal locationController;

    @EJB
    private TypeControllerLocal typeController;

    @EJB
    private CategoryControllerLocal categoryController;

    @EJB
    private ExperienceControllerLocal experienceController;
    
    private List<Experience> experienceEntities;
    private List<Type> typeEntities;
    private List<Category> categoryEntities;
    private List<Location> locationEntities;
    
    private Category selectedCategoryEntity;
    private Type selectedTypeEntity;
    private Location selectedLocationEntity;
    private Date selectedDateEntity;
    private Experience selectedExperienceToView;
    private int numOfPeople;
    
    private String searchString;

    /**
     * Creates a new instance of ViewAllExperiencesManagedBean
     */
    public ViewAllExperiencesManagedBean() {
        numOfPeople = 1;
    }
    
    @PostConstruct
    public void PostConstruct(){
        experienceEntities = experienceController.retrieveAllExperiences();
        categoryEntities = categoryController.retrieveAllCategories();
        typeEntities = typeController.retrieveAllTypes();
        locationEntities = locationController.retrieveAllLocations();
        
        if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("searchDate")){
            selectedDateEntity = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchDate");
            numOfPeople = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchNumOfPeople");
        }
        
        
    }
    
    @PreDestroy
    public void preDestroy(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("searchDate");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("searchNumOfPeople");
    }
    
    public void filterExperience(){
        Experience e = new Experience();
        e.setTitle("Experience 2");
        experienceEntities.add(e);
//        experienceEntities.clear();
//        if(searchString != null && searchString.trim().length() > 0){
//            experienceEntities = experienceController.retrieveExperienceByName(searchString);
//        } else {experienceEntities = experienceController.retrieveAllExperiences();}
//        
//        if(selectedDateEntity!=null){
//            List<Experience> filter = experienceController.retrieveExperienceBySingleDate(selectedDateEntity);
//            ListIterator iter = experienceEntities.listIterator();
//            while(iter.hasNext()){
//                if(!filter.contains(iter.next())){
//                    iter.remove();
//                }
//            }
//        }
//        if(selectedTypeEntity!=null){
//            List<Experience> filter = experienceController.retrieveExperienceByType(selectedTypeEntity);
//            ListIterator iter = experienceEntities.listIterator();
//            while(iter.hasNext()){
//                if(!filter.contains(iter.next())){
//                    iter.remove();
//                }
//            }
//        }
//        if(selectedCategoryEntity!=null){
//            List<Experience> filter = new ArrayList<>();
//            ListIterator iter = experienceEntities.listIterator();
//            while(iter.hasNext()){
//                if(!filter.contains(iter.next())){
//                    iter.remove();
//                }
//            }
//        }
//        if(selectedLocationEntity!=null){
//            List<Experience> filter = experienceController.retrieveExperienceByLocation(selectedLocationEntity);
//            ListIterator iter = experienceEntities.listIterator();
//            while(iter.hasNext()){
//                if(!filter.contains(iter.next())){
//                    iter.remove();
//                }
//            }
//        }
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

    public LocationControllerLocal getLocationController() {
        return locationController;
    }

    public Experience getSelectedExperienceToView() {
        return selectedExperienceToView;
    }

    public void setSelectedExperienceToView(Experience selectedExperienceToView) {
        this.selectedExperienceToView = selectedExperienceToView;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("experienceEntityToView", selectedExperienceToView);
    }

    public void setLocationController(LocationControllerLocal locationController) {
        this.locationController = locationController;
    }

    public TypeControllerLocal getTypeController() {
        return typeController;
    }

    public void setTypeController(TypeControllerLocal typeController) {
        this.typeController = typeController;
    }

    public CategoryControllerLocal getCategoryController() {
        return categoryController;
    }

    public void setCategoryController(CategoryControllerLocal categoryController) {
        this.categoryController = categoryController;
    }

    public ExperienceControllerLocal getExperienceController() {
        return experienceController;
    }

    public void setExperienceController(ExperienceControllerLocal experienceController) {
        this.experienceController = experienceController;
    }

    public List<Experience> getExperienceEntities() {
        return experienceEntities;
    }

    public void setExperienceEntities(List<Experience> experienceEntities) {
        this.experienceEntities = experienceEntities;
    }

    public List<Type> getTypeEntities() {
        return typeEntities;
    }

    public void setTypeEntities(List<Type> typeEntities) {
        this.typeEntities = typeEntities;
    }

    public List<Category> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<Category> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public List<Location> getLocationEntities() {
        return locationEntities;
    }

    public void setLocationEntities(List<Location> locationEntities) {
        this.locationEntities = locationEntities;
    }

    
    public Category getSelectedCategoryEntity() {
        return selectedCategoryEntity;
    }

    public void setSelectedCategoryEntity(Category selectedCategoryEntity) {
        this.selectedCategoryEntity = selectedCategoryEntity;
    }

    public Type getSelectedTypeEntity() {
        return selectedTypeEntity;
    }

    public void setSelectedTypeEntity(Type selectedTypeEntity) {
        this.selectedTypeEntity = selectedTypeEntity;
    }

    public Location getSelectedLocationEntity() {
        return selectedLocationEntity;
    }

    public void setSelectedLocationEntity(Location selectedLocationEntity) {
        this.selectedLocationEntity = selectedLocationEntity;
    }

    public Date getSelectedDateEntity() {
        return selectedDateEntity;
    }

    public void setSelectedDateEntity(Date selectedDateEntity) {
        this.selectedDateEntity = selectedDateEntity;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public int getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(int numOfPeople) {
        this.numOfPeople = numOfPeople;
    }
}