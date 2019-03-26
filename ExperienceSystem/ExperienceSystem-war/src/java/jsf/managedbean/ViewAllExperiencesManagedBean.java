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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
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
public class ViewAllExperiencesManagedBean {

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
    
    private List<SelectItem> categorySelectItems;
    private List<SelectItem> typeSelectItems;
    private List<SelectItem> locationSelectItems;
    
    private Category selectedCategoryEntity;
    private Type selectedTypeEntity;
    private Location selectedLocationEntity;
    private List<Date> selectedDateEntities;
    
    private String searchString;

    public LocationControllerLocal getLocationController() {
        return locationController;
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

    public List<SelectItem> getCategorySelectItems() {
        return categorySelectItems;
    }

    public void setCategorySelectItems(List<SelectItem> categorySelectItems) {
        this.categorySelectItems = categorySelectItems;
    }

    public List<SelectItem> getTypeSelectItems() {
        return typeSelectItems;
    }

    public void setTypeSelectItems(List<SelectItem> typeSelectItems) {
        this.typeSelectItems = typeSelectItems;
    }

    public List<SelectItem> getLocationSelectItems() {
        return locationSelectItems;
    }

    public void setLocationSelectItems(List<SelectItem> locationSelectItems) {
        this.locationSelectItems = locationSelectItems;
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

    public List<Date> getSelectedDateEntities() {
        return selectedDateEntities;
    }

    public void setSelectedDateEntities(List<Date> selectedDateEntities) {
        this.selectedDateEntities = selectedDateEntities;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
    
    

    /**
     * Creates a new instance of ViewAllExperiencesManagedBean
     */
    public ViewAllExperiencesManagedBean() {
    }
    
    @PostConstruct
    public void PostConstruct(){
        experienceEntities = experienceController.retrieveAllExperiences();
        categoryEntities = categoryController.retrieveAllCategories();
        typeEntities = typeController.retrieveAllTypes();
        locationEntities = locationController.retrieveAllLocations();
        
        locationSelectItems = new ArrayList<>();
        typeSelectItems = new ArrayList<>();
        categorySelectItems = new ArrayList<>();
        
        for(Type typeEntity:typeEntities)
        {
            typeSelectItems.add(new SelectItem(typeEntity, typeEntity.getType(), typeEntity.getType()));
        }
        
        for(Category categoryEntity: categoryEntities){
            categorySelectItems.add(new SelectItem(categoryEntity, categoryEntity.getCategory(), categoryEntity.getCategory()));
        }
        
        for(Location locationEntity: locationEntities){
            locationSelectItems.add(new SelectItem(locationEntity, locationEntity.getLocation(), locationEntity.getLocation()));
        }
    }
    
    public void filterExperience(){
        
        if(searchString != null && searchString.trim().length() > 0){
            experienceEntities = experienceController.retrieveExperienceByName(searchString);
        } else {experienceEntities = experienceController.retrieveAllExperiences();}
        
        if(selectedDateEntities!=null && selectedDateEntities.size()>0){
            List<Experience> filter = experienceController.retrieveExperienceByDate(minDate(selectedDateEntities), maxDate(selectedDateEntities));
            ListIterator iter = experienceEntities.listIterator();
            while(iter.hasNext()){
                if(!filter.contains(iter.next())){
                    iter.remove();
                }
            }
        }
        if(selectedTypeEntity!=null){
            List<Experience> filter = experienceController.retrieveExperienceByType(selectedTypeEntity);
            ListIterator iter = experienceEntities.listIterator();
            while(iter.hasNext()){
                if(!filter.contains(iter.next())){
                    iter.remove();
                }
            }
        }
        if(selectedCategoryEntity!=null){
            List<Experience> filter = experienceController.retrieveExperienceByCategory(selectedCategoryEntity);
            ListIterator iter = experienceEntities.listIterator();
            while(iter.hasNext()){
                if(!filter.contains(iter.next())){
                    iter.remove();
                }
            }
        }
        if(selectedLocationEntity!=null){
            List<Experience> filter = experienceController.retrieveExperienceByLocation(selectedLocationEntity);
            ListIterator iter = experienceEntities.listIterator();
            while(iter.hasNext()){
                if(!filter.contains(iter.next())){
                    iter.remove();
                }
            }
        }
    }
    
    public void viewExperienceDetails(ActionEvent event) throws IOException{
        Long experienceIdToView = (Long)event.getComponent().getAttributes().get("experienceId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceIdToView", experienceIdToView);
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
}
