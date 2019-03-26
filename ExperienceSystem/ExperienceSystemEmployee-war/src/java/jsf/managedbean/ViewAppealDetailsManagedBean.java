/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Appeal;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.EmployeeControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewAppealDetailsManagedBean")
@ViewScoped
public class ViewAppealDetailsManagedBean implements Serializable {

    @EJB(name = "EmployeeControllerLocal")
    private EmployeeControllerLocal employeeControllerLocal;

    private Long appealIdToView;
    private Appeal appealToView;
    private String backMode; 
    
    public ViewAppealDetailsManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        appealIdToView = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("appealIdToView");
        backMode = (String)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("backMode");
        
        try
        {            
            
            appealToView = employeeControllerLocal.retrieveAppealById(appealIdToView);
            
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
        
    }
    
    public void back(ActionEvent event) throws IOException
    {
        if(backMode == null || backMode.trim().length() == 0)
        {
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllAppeals.xhtml");
        }
        else
        {
            FacesContext.getCurrentInstance().getExternalContext().redirect(backMode + ".xhtml");
        }
    }
    
    public void foo(){
        
    }
    
    public void processAppeal(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("appealIdToProcess", appealIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("processAppeal.xhtml");
    }

    public Long getAppealIdToView() {
        return appealIdToView;
    }

    public void setAppealIdToView(Long appealIdToView) {
        this.appealIdToView = appealIdToView;
    }

    public Appeal getAppealToView() {
        return appealToView;
    }

    public void setAppealToView(Appeal appealToView) {
        this.appealToView = appealToView;
    }

    public String getBackMode() {
        return backMode;
    }

    public void setBackMode(String backMode) {
        this.backMode = backMode;
    }

            
}
