/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Category;
import entity.Experience;
import entity.Language;
import entity.Type;
import entity.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import stateless.CategoryControllerLocal;
import stateless.ExperienceControllerLocal;
import stateless.LanguageControllerLocal;
import stateless.TypeControllerLocal;
import stateless.UserControllerLocal;
import util.exception.CreateNewExperienceException;
import util.exception.InputDataValidationException;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Named(value = "createNewExperience")
@RequestScoped
public class CreateNewExperience {

    @EJB
    private UserControllerLocal userController;

    @EJB
    private LanguageControllerLocal languageController;

    @EJB
    private TypeControllerLocal typeController;

    @EJB
    private CategoryControllerLocal categoryController;

    @EJB
    private ExperienceControllerLocal experienceController;

    private Experience newExperience;
    private String city;
    private Long catId, typeId, langId;
    private List<Category> lsCat;
    private List<Type> lsType;
    private List<Language> lsLang;
            
    public CreateNewExperience() {
    }
    
    @PostConstruct
    public void postConstruct() {
        try {
            newExperience = new Experience();
            lsCat = categoryController.retrieveAllCategories();
            lsType = typeController.retrieveAllTypes();
            lsLang = languageController.retrieveAllLanguages();
        } catch (Exception ex) {
            Logger.getLogger(CreateNewExperience.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public String create() {
        try {
            User u = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserEntity");
            u = userController.retrieveUserById(u.getUserId());
            newExperience.setHost(u);
            Experience e = experienceController.createExpWithLangTypeCat(newExperience, getCatId(), getTypeId(), getLangId());    
            List<Experience> lss = u.getExperienceHosted();
            lss.add(e);
            u.setExperienceHosted(lss);
            userController.update(u);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New experience created successfully (Exp ID: " + e.getExperienceId()+ ")", null));
            return "viewAllHostExperiences";
            
        } catch (CreateNewExperienceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CreateNewExperienceException", null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong info", null));
        } catch (UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not Found", null));
        }
        return "createNewHostExperience";
    }

    public Experience getNewExperience() {
        return newExperience;
    }

    public void setNewExperience(Experience newExperience) {
        this.newExperience = newExperience;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Category> getLsCat() {
        return lsCat;
    }

    public void setLsCat(List<Category> lsCat) {
        this.lsCat = lsCat;
    }

    public List<Type> getLsType() {
        return lsType;
    }

    public void setLsType(List<Type> lsType) {
        this.lsType = lsType;
    }

    public List<Language> getLsLang() {
        return lsLang;
    }

    public void setLsLang(List<Language> lsLang) {
        this.lsLang = lsLang;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getLangId() {
        return langId;
    }

    public void setLangId(Long langId) {
        this.langId = langId;
    }
    
}
