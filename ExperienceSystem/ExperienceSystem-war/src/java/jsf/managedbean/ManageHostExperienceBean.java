/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Booking;
import entity.Evaluation;
import entity.Experience;
import entity.ExperienceDate;
import entity.User;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import stateless.EvaluationControllerLocal;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;
import stateless.UserControllerLocal;
import util.exception.CreateNewExperienceDateException;
import util.exception.CreateNewExperienceException;
import util.exception.ExperienceDateNotActiveException;
import util.exception.ExperienceNotActiveException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author samue
 */
@Named(value = "manageHostExperienceBean")
@ViewScoped
public class ManageHostExperienceBean implements Serializable {

    @EJB
    private UserControllerLocal userController;

    @EJB
    private EvaluationControllerLocal evaluationController;

    @EJB
    private ExperienceDateControllerLocal experienceDateController;

    @EJB
    private ExperienceControllerLocal experienceController;

    private Experience currentExperience;
    private Long catId, typeId, langId;

    private Date date;
    private int capacity;
    private BigDecimal price;

    private ExperienceDate currentExpDate;
    private List<User> guests = new ArrayList<>();

    private int rating;
    private String remark;

    public ManageHostExperienceBean() {
    }

    @PostConstruct
    public void dd() {
        currentExperience = (Experience) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("exp");
    }

    public void redir(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("manageHostExperience.xhtml");
        this.currentExperience = (Experience) event.getComponent().getAttributes().get("exp");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("exp", currentExperience);
    }

    public void evaluate(ActionEvent event) throws IOException {
        User currentGuest = (User) event.getComponent().getAttributes().get("g");
        User loggedIn = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserEntity");
        Evaluation e = new Evaluation();
        e.setScore(BigDecimal.valueOf((double) rating));
        e.setRemark(remark);
        e.setUserBeingEvaluated(currentGuest);
        e.setUserEvaluating(loggedIn);
        e.setEvaluationTime(LocalDate.now());
        e = evaluationController.create(e);
        List<Evaluation> lsE = loggedIn.getEvaluations();
        lsE.add(e);
        loggedIn.setEvaluations(lsE);
        userController.update(loggedIn);
        FacesContext.getCurrentInstance().getExternalContext().redirect("manageHostExperience.xhtml");
    }

    public void deleteExpDate(ActionEvent event) throws IOException {
        System.out.println(currentExpDate + "(1)");
        if (currentExpDate == null) {
            System.out.println(currentExpDate + "(2a)");
            setExpDate(event);
        }
        System.out.println(currentExpDate + "(3)");
        try {
            experienceDateController.deleteExperienceDate(currentExpDate.getExperienceDateId(), "");
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
            experienceDateController.updateExperienceDate(currentExpDate);
        } catch (InputDataValidationException | CreateNewExperienceException ex) {
            Logger.getLogger(ManageHostExperienceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", null));
    }

    public void setExpDate(ActionEvent event) {
        ExperienceDate ed = (ExperienceDate) event.getComponent().getAttributes().get("d");
        ed = experienceDateController.retrieveExperienceDateByDateId(ed.getExperienceDateId());
        for (Booking b : ed.getBookings()) {
            if (b.isHostEvaluated()) {
                guests.add(b.getUser());
            }
        }
        setCurrentExpDate(ed);
    }

   
    public void addDate() throws IOException {
        ExperienceDate ed = new ExperienceDate();
        ed.setActive(true);
        ed.setStartDate(date);
        ed.setCapacity(getCapacity());
        ed.setPrice(getPrice());
        ed.setExperience(getCurrentExperience());
        try {
            ed = experienceDateController.createNewExperienceDate(ed);
        } catch (CreateNewExperienceDateException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CreateNewExperienceDateException", null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "InputDataValidationException", null));
        }
        List<ExperienceDate> lss = getCurrentExperience().getExperienceDates();
        lss.add(ed);
        currentExperience.setExperienceDates(lss);
        try {
            experienceController.updateExperienceInformation(currentExperience);
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "InputDataValidationException", null));
        } catch (ExperienceNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ExperienceNotFoundException", null));
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> s = e.getConstraintViolations();
            for (Iterator<ConstraintViolation<?>> it = e.getConstraintViolations().iterator(); it.hasNext();) {
                ConstraintViolation<? extends Object> v = it.next();
                System.err.println(v);
                System.err.println("==>>" + v.getMessage());
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ConstraintViolationException", null));
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("manageHostExperience.xhtml?id=1");
    }

    public void deleteExp(ActionEvent event) throws IOException {
//        Long id = (Long) event.getComponent().getAttributes().get("id");
//        String reason = (String) event.getComponent().getAttributes().get("reason");
        System.out.println("In managehostexpbean" + currentExperience + "-------");
        Long id = getCurrentExperience().getExperienceId();
        String reason = "";
        try {
            experienceController.deleteExperience(id, reason);
        } catch (ExperienceNotActiveException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exp Not Active", null));
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllHostExperiences.xhtml");
    }

    public void updateExp() {
        try {
            experienceController.updateExperienceWithCatTypeLang(currentExperience, catId, typeId, langId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", null));
        } catch (InputDataValidationException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong info", null));
        }
    }

    public ExperienceControllerLocal getExperienceController() {
        return experienceController;
    }

    public Experience getCurrentExperience() {
        return currentExperience;
    }

    public void setCurrentExperience(Experience currentExperience) {
        this.currentExperience = currentExperience;
    }

    public Long getCatId() {
        this.catId = getCurrentExperience().getCategory().getCategoryId();
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public Long getTypeId() {
        this.typeId = getCurrentExperience().getType().getTypeId();
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getLangId() {
        this.langId = getCurrentExperience().getLanguage().getLanguageId();
        return langId;
    }

    public void setLangId(Long langId) {
        this.langId = langId;
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
        System.out.println(currentExpDate + "(4)");
        return currentExpDate;
    }

    public void setCurrentExpDate(ExperienceDate currentExpDate) {
        System.out.println(currentExpDate + "(a)");
        this.currentExpDate = currentExpDate;
        System.out.println(currentExpDate + "(b)");
    }

    public List<User> getGuests() {
        if (guests.isEmpty()) {
            return null;
        }
        return guests;
    }

    public void setGuests(List<User> guests) {
        this.guests = guests;
    }

}
