/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.ExperienceDate;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.ExperienceDateControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewExperienceDateDetailsManagedBean")
@ViewScoped
public class ViewExperienceDateDetailsManagedBean {

    @EJB(name = "ExperienceDateControllerLocal")
    private ExperienceDateControllerLocal experienceDateControllerLocal;

    private Long experienceDateIdToView;
    private String backMode;
    private ExperienceDate experienceDateToView;
           
    public ViewExperienceDateDetailsManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        
        experienceDateIdToView = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("experienceDateIdToView");
        backMode = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("backMode");
        
        try{
            
            experienceDateToView = experienceDateControllerLocal.retrieveExperienceDateByDateId(experienceDateIdToView);
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: "+ ex.getMessage(), null));
            
        }
    }
    
    public void back(ActionEvent event) throws IOException{
        if(backMode == null || backMode.trim().length() == 0){
            
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllExperienceDates.xhtml");
            
        }else{
            
            FacesContext.getCurrentInstance().getExternalContext().redirect(backMode + ".xhtml");
            
        }
    }
    
    public void foo(){
    }
    
    public void updateExperienceDate(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceDateIdToUpdate", experienceDateIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateExperienceDate.xhtml");
    }
    
    public void deleteExperienceDate(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("experienceDateIdToDelete", experienceDateIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("deleteExperienceDate.xhtml");
    }

    public Long getExperienceDateIdToView() {
        return experienceDateIdToView;
    }

    public void setExperienceDateIdToView(Long experienceDateIdToView) {
        this.experienceDateIdToView = experienceDateIdToView;
    }

    public String getBackMode() {
        return backMode;
    }

    public void setBackMode(String backMode) {
        this.backMode = backMode;
    }

    public ExperienceDate getExperienceDateToView() {
        return experienceDateToView;
    }

    public void setExperienceDateToView(ExperienceDate experienceDateToView) {
        this.experienceDateToView = experienceDateToView;
    }
    
    
}
