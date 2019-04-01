/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Booking;
import entity.Category;
import entity.Experience;
import entity.ExperienceDate;
import entity.Language;
import entity.Location;
import entity.Type;
import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import stateless.CategoryControllerLocal;
import stateless.EvaluationControllerLocal;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;
import stateless.LanguageControllerLocal;
import stateless.LocationControllerLocal;
import stateless.TypeControllerLocal;
import stateless.UserControllerLocal;
import util.exception.CreateNewExperienceDateException;
import util.exception.CreateNewExperienceException;
import util.exception.DeleteExperienceException;
import util.exception.ExperienceDateNotActiveException;
import util.exception.ExperienceNotActiveException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateEperienceInfoException;

/**
 *
 * @author samue
 */
@Named(value = "manageHostExperienceManagedBean")
@ViewScoped
public class ManageHostExperienceManagedBean implements Serializable {

    @EJB
    private UserControllerLocal userControllerLocal;
    @EJB
    private EvaluationControllerLocal evaluationControllerLocal;
    @EJB
    private ExperienceDateControllerLocal experienceDateControllerLocal;
    @EJB
    private ExperienceControllerLocal experienceControllerLocal;
    @EJB
    private LanguageControllerLocal languageControllerLocal;
    @EJB
    private TypeControllerLocal typeControllerLocal;
    @EJB
    private CategoryControllerLocal categoryControllerLocal;
    @EJB
    private LocationControllerLocal locationControllerLocal;

    private Long hostExperienceIdToView;
    private Experience hostExperience;
    private Long categoryId, typeId, languageId, locationId;
    private List<Category> categories;
    private List<Type> types;
    private List<Language> languages;
    private List<Location> locations;

    private Date date;
    private int capacity;
    private BigDecimal price;

    private ExperienceDate currentExpDate;
    private List<User> guests = new ArrayList<>();

    private int rating;
    private String remark;

    public ManageHostExperienceManagedBean() {
        
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("******** ManageHostExperienceManagedBean: postConstruct");
        categories = categoryControllerLocal.retrieveAllCategories();
        types = typeControllerLocal.retrieveAllTypes();
        languages = languageControllerLocal.retrieveAllLanguages();
        locations = locationControllerLocal.retrieveAllLocations();
        
        hostExperienceIdToView = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("hostExperienceIdToView");
        System.out.println("    **** hostExperienceIdToView: " + hostExperienceIdToView);
        try {
            hostExperience = experienceControllerLocal.retrieveExperienceById(hostExperienceIdToView);
            System.out.println("    **** hostExperienceId: " + hostExperience.getExperienceId());
        } catch (ExperienceNotFoundException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "The experience is unvailable", null));
        }   
    }
    

    public void updateExperienceDetails(ActionEvent event) {
        try {
            
            Category category = new Category();
            category.setCategoryId(categoryId);
            Type type = new Type();
            type.setTypeId(typeId);
            Language language = new Language();
            language.setLanguageId(languageId);
            Location location = new Location();
            location.setLocationId(locationId);
            hostExperience.setCategory(category);
            hostExperience.setType(type);
            hostExperience.setLanguage(language);
            hostExperience.setLocation(location);
            
            experienceControllerLocal.updateExperienceInformation(hostExperience);
            hostExperience = experienceControllerLocal.retrieveExperienceById(hostExperience.getExperienceId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Experience information updated successfully!", null));
        } catch (UpdateEperienceInfoException ex) {
            try {
                hostExperience = experienceControllerLocal.retrieveExperienceById(hostExperienceIdToView);
            } catch (ExperienceNotFoundException ex2) {
                
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }  catch (ExperienceNotFoundException ex2) {         
        }
    }
    
    public void deleteExperience(ActionEvent event) throws IOException {
        try {
            experienceControllerLocal.deleteExperience(hostExperience.getExperienceId());
        } catch (DeleteExperienceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllHostExperiences.xhtml");
    }
    
    public void redir(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("manageHostExperience.xhtml");
        this.hostExperience = (Experience) event.getComponent().getAttributes().get("exp");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("exp", hostExperience);
    }

    public void evaluate(ActionEvent event) throws IOException {
//        User currentGuest = (User) event.getComponent().getAttributes().get("g");
//        User loggedIn = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserEntity");
//        Evaluation e = new Evaluation();
//        e.setScore(BigDecimal.valueOf((double) rating));
//        e.setRemark(remark);
//        e.setUserBeingEvaluated(currentGuest);
//        e.setUserEvaluating(loggedIn);
//        e.setEvaluationTime(LocalDate.now());
//        e = evaluationController.create(e);
//        List<Evaluation> lsE = loggedIn.getEvaluations();
//        lsE.add(e);
//        loggedIn.setEvaluations(lsE);
//        userController.update(loggedIn);
//        FacesContext.getCurrentInstance().getExternalContext().redirect("manageHostExperience.xhtml");
    }

    public void deleteExpDate(ActionEvent event) throws IOException {
        System.out.println(currentExpDate + "(1)");
        if (currentExpDate == null) {
            System.out.println(currentExpDate + "(2a)");
            setExpDate(event);
        }
        System.out.println(currentExpDate + "(3)");
        try {
            experienceDateControllerLocal.deleteExperienceDate(currentExpDate.getExperienceDateId(), "");
        } catch (ExperienceDateNotActiveException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ExperienceDateNotActiveException", null));
        }
    }

    public void updateExpDate(ActionEvent event) throws IOException {
        if (currentExpDate == null) {
            setExpDate(event);
        }
        System.out.println(currentExpDate.getStartDate());
        try {
            experienceDateControllerLocal.updateExperienceDate(currentExpDate);
        } catch (InputDataValidationException | CreateNewExperienceException ex) {
            
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", null));
    }

    public void setExpDate(ActionEvent event) {
//        ExperienceDate ed = (ExperienceDate) event.getComponent().getAttributes().get("d");
//        ed = experienceDateControllerLocal.retrieveExperienceDateByDateId(ed.getExperienceDateId());
//        for (Booking b : ed.getBookings()) {
//            if (b.isHostEvaluated()) {
//                guests.add(b.getGuest());
//            }
//        }
//        setCurrentExpDate(ed);
    }

   
    public void addDate() throws IOException {
//        ExperienceDate ed = new ExperienceDate();
//        ed.setActive(true);
//        ed.setStartDate(date);
//        ed.setCapacity(getCapacity());
//        ed.setPrice(getPrice());
//        ed.setExperience(getHostExperience());
//        try {
//            ed = experienceDateControllerLocal.createNewExperienceDate(ed);
//        } catch (CreateNewExperienceDateException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CreateNewExperienceDateException", null));
//        } catch (InputDataValidationException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "InputDataValidationException", null));
//        }
//        List<ExperienceDate> lss = getHostExperience().getExperienceDates();
//        lss.add(ed);
//        hostExperience.setExperienceDates(lss);
//        try {
//            experienceControllerLocal.updateExperienceInformation(hostExperience);
//        } catch (InputDataValidationException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "InputDataValidationException", null));
//        } catch (ExperienceNotFoundException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ExperienceNotFoundException", null));
//        } catch (ConstraintViolationException e) {
//            Set<ConstraintViolation<?>> s = e.getConstraintViolations();
//            for (Iterator<ConstraintViolation<?>> it = e.getConstraintViolations().iterator(); it.hasNext();) {
//                ConstraintViolation<? extends Object> v = it.next();
//                System.err.println(v);
//                System.err.println("==>>" + v.getMessage());
//            }
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ConstraintViolationException", null));
//        }
//        FacesContext.getCurrentInstance().getExternalContext().redirect("manageHostExperience.xhtml?id=1");
    }



    public Long getHostExperienceIdToView() {
        return hostExperienceIdToView;
    }

    public void setHostExperienceIdToView(Long hostExperienceIdToView) {
        this.hostExperienceIdToView = hostExperienceIdToView;
    }

    public Experience getHostExperience() {
        return hostExperience;
    }

    public void setHostExperience(Experience hostExperience) {
        this.hostExperience = hostExperience;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ExperienceDate getCurrentExpDate() {
        return currentExpDate;
    }

    public void setCurrentExpDate(ExperienceDate currentExpDate) {
        this.currentExpDate = currentExpDate;
    }

    public List<User> getGuests() {
        return guests;
    }

    public void setGuests(List<User> guests) {
        this.guests = guests;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    
}
