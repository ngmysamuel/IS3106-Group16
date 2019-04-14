/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Experience;
import entity.ExperienceDate;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.BookingControllerLocal;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;
import util.exception.ExperienceDateNotFoundException;
import util.exception.ExperienceNotFoundException;

/**
 *
 * @author Asus
 */
@Named(value = "viewExperienceDetailsEmployeeManagedBean")
@ViewScoped
public class ViewExperienceDetailsEmployeeManagedBean implements Serializable {

    @EJB
    private BookingControllerLocal bookingControllerLocal;
    @EJB
    private ExperienceDateControllerLocal experienceDateControllerLocal;
    @EJB
    private ExperienceControllerLocal experienceControllerLocal;
    
    // basic Experience details information
    private Experience experience;
    private List<String> images;
    private ExperienceDate selectedExperienceDateToView;
    

    public ViewExperienceDetailsEmployeeManagedBean() {
        images = new ArrayList();
        images.add("https://i.imgur.com/2hocHvd.jpg");
        images.add("https://i.imgur.com/R6wtAtN.jpg");
        images.add("https://i.imgur.com/slbHL1Z.jpg");
        images.add("https://i.imgur.com/SQiLyd4.jpg");
    }
    
    @PostConstruct
    public void postConstruct(){
        System.out.println("******** ViewExperienceDetailsEmployeeManagedBean: postConstruct()");
        Long experienceIdToView = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("experienceIdToView");
        System.out.println("**** experienceIdToView: " + experienceIdToView);

        // retrieve experience information
        try{
            experience = experienceControllerLocal.retrieveExperienceById(experienceIdToView);
        } catch(ExperienceNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving experience info: " + ex.getMessage(), null));
        }
        System.out.println("-----------------------------");
    }
    
    public void doViewExperienceDateDetails(ActionEvent event) {
        System.out.println("******** ViewExperienceDetailsEmployeeManagedBean: doViewExperienceDateDetails()");
        Long experienceDateIdToView = (Long)event.getComponent().getAttributes().get("selectedExperienceDateIdToView");
        System.out.println("**** selectedExperienceDateIdToView: " + experienceDateIdToView);
        try {
            selectedExperienceDateToView = experienceDateControllerLocal.retrieveExperienceDateByExperienceDateId(experienceDateIdToView);
        } catch(ExperienceDateNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving experience date info: " + ex.getMessage(), null));
        }
    }
    
    public String convertDateToString(Date date) {
        if(date != null) {
            SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");   
            return formatter.format(date);
        } else {
            return null;
        } 
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public ExperienceDate getSelectedExperienceDateToView() {
        return selectedExperienceDateToView;
    }

    public void setSelectedExperienceDateToView(ExperienceDate selectedExperienceDateToView) {
        this.selectedExperienceDateToView = selectedExperienceDateToView;
    }

}
