/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Experience;
import entity.ExperienceDate;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;
import util.exception.CreateNewExperienceDateException;
import util.exception.ExperienceNotActiveException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author samue
 */
@Named(value = "manageHostExperienceManagedBean")
@RequestScoped
public class ManageHostExperienceManagedBean {
//
//    @EJB
//    private ExperienceDateControllerLocal experienceDateController;
//
//    @EJB
//    private ExperienceControllerLocal experienceController;
//
//    private Experience currentExperience;
//    private Long catId, typeId, langId;
//    
//    private Date date;
//    private int capacity;
//    private BigDecimal price;
//    
//    public ManageHostExperienceManagedBean() {
//    }
//    
//    
//    public void addDate() throws IOException {
//        ExperienceDate ed = new ExperienceDate();
//        ed.setActive(true);
//        LocalDate d = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        ed.setStartDate(d);
//        ed.setCapacity(getCapacity());
//        ed.setPrice(getPrice());
//        ed.setExperience(getCurrentExperience());
//        try {
//            ed = experienceDateController.createNewExperienceDate(ed);
//        } catch (CreateNewExperienceDateException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CreateNewExperienceDateException", null));
//        } catch (InputDataValidationException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "InputDataValidationException", null));
//        }
//        List<ExperienceDate> lss = getCurrentExperience().getExperienceDates();
//        lss.add(ed);
//        currentExperience.setExperienceDates(lss);
//        try {
//            experienceController.updateExperienceInformation(currentExperience);
//        } catch (InputDataValidationException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "InputDataValidationException", null));
//        } catch (ExperienceNotFoundException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ExperienceNotFoundException", null));
//        }
//        FacesContext.getCurrentInstance().getExternalContext().redirect("manageHostExperience.xhtml?id=1");
//    }
//    
//    public void deleteExp(ActionEvent event) throws IOException {
////        Long id = (Long) event.getComponent().getAttributes().get("id");
////        String reason = (String) event.getComponent().getAttributes().get("reason");
//System.out.println("In managehostexpbean"+currentExperience+"-------");
//        Long id = getCurrentExperience().getExperienceId();
//        String reason = "";
//        try {
//            experienceController.deleteExperience(id, reason);
//        } catch (ExperienceNotActiveException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exp Not Active", null));
//        }
//        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllHostExperiences.xhtml");
//    }
//    
//    
//    public void updateExp() {
//        try {
//            experienceController.updateExperienceInformation(getCurrentExperience());
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", null));
//        } catch (InputDataValidationException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data entered is wrong", null));
//        } catch (ExperienceNotFoundException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Exp", null));
//        }
//    }
//
//    public ExperienceControllerLocal getExperienceController() {
//        return experienceController;
//    }
//
//    public Experience getCurrentExperience() {
//        Experience e = (Experience) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentExp");
//        e = experienceController.retrieveExperienceById(e.getExperienceId());
//        this.currentExperience = e;
//        return e;
//    }
//
//    public void setCurrentExperience(Experience currentExperience) {
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentExp", currentExperience);
//        this.currentExperience = currentExperience;
//    }
//
//    public Long getCatId() {
//        this.catId = getCurrentExperience().getCategory().getCategoryId();
//        return catId;
//    }
//
//    public void setCatId(Long catId) {
//        this.catId = catId;
//    }
//
//    public Long getTypeId() {
//        this.typeId = getCurrentExperience().getType().getTypeId();
//        return typeId;
//    }
//
//    public void setTypeId(Long typeId) {
//        this.typeId = typeId;
//    }
//
//    public Long getLangId() {
//        this.langId = getCurrentExperience().getLanguage().getLanguageId();
//        return langId;
//    }
//
//    public void setLangId(Long langId) {
//        this.langId = langId;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
//
//    public int getCapacity() {
//        return capacity;
//    }
//
//    public void setCapacity(int capacity) {
//        this.capacity = capacity;
//    }
//
//    public BigDecimal getPrice() {
//        return price;
//    }
//
//    public void setPrice(BigDecimal price) {
//        this.price = price;
//    }
}
