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
import entity.User;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import stateless.CategoryControllerLocal;
import stateless.ExperienceControllerLocal;
import stateless.LanguageControllerLocal;
import stateless.LocationControllerLocal;
import stateless.TypeControllerLocal;
import stateless.UserControllerLocal;
import util.exception.CreateNewExperienceException;
import util.exception.InputDataValidationException;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Named(value = "createNewExperienceManagedBean")
@RequestScoped
public class CreateNewExperienceManagedBean {

    @EJB
    private UserControllerLocal userControllerLocal;
    @EJB
    private LanguageControllerLocal languageControllerLocal;
    @EJB
    private TypeControllerLocal typeControllerLocal;
    @EJB
    private CategoryControllerLocal categoryControllerLocal;
    @EJB
    private LocationControllerLocal locationControllerLocal;
    @EJB
    private ExperienceControllerLocal experienceControllerLocal;
    

    private Experience newExperience;
    private Long categoryId, typeId, languageId, locationId;
    private List<Category> categories;
    private List<Type> types;
    private List<Language> languages;
    private List<Location> locations;
            
    public CreateNewExperienceManagedBean() {
        newExperience = new Experience();
    }
    
    @PostConstruct
    public void postConstruct() {
        categories = categoryControllerLocal.retrieveAllCategories();
        types = typeControllerLocal.retrieveAllTypes();
        languages = languageControllerLocal.retrieveAllLanguages();
        locations = locationControllerLocal.retrieveAllLocations();
    }
    
    public void createNewExperience() {
        try {
            User currentUser = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
            currentUser = userControllerLocal.retrieveUserById(currentUser.getUserId());
            newExperience.setHost(currentUser);
            
            Category category = new Category();
            category.setCategoryId(categoryId);
            Type type = new Type();
            type.setTypeId(typeId);
            Language language = new Language();
            language.setLanguageId(languageId);
            Location location = new Location();
            location.setLocationId(locationId);
            newExperience.setCategory(category);
            newExperience.setType(type);
            newExperience.setLanguage(language);
            newExperience.setLocation(location);
            
            newExperience = experienceControllerLocal.createNewExperience(newExperience);   
            currentUser.getExperienceHosted().add(newExperience);    
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New experience created successfully (Exp ID: " + newExperience.getExperienceId()+ ")", null));            
        } catch (CreateNewExperienceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        } catch (UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

    public Experience getNewExperience() {
        return newExperience;
    }

    public void setNewExperience(Experience newExperience) {
        this.newExperience = newExperience;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
    
}
