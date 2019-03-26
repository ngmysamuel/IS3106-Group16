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
@Named(value = "processAppealManagedBean")
@ViewScoped
public class ProcessAppealManagedBean implements Serializable {

    @EJB(name = "EmployeeControllerLocal")
    private EmployeeControllerLocal employeeControllerLocal;
    
    private Long appealIdToUpdate;
    private Appeal appealToUpdate;
    private String reply;
    private Long employeeId; 
    
    
    public ProcessAppealManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        
        appealIdToUpdate = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("appealIdToUpdate");
        employeeId = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("employeeId");
        reply = (String)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("reply");
        
        try{
            
            appealToUpdate = employeeControllerLocal.retrieveAppealById(appealIdToUpdate);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the appeal details: " + ex.getMessage(), null));
        
        }
    }
    
    public void back(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("appealIdToView", appealIdToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAppealDetails.xhtml");
    }
    
    public void foo(){
        
    }
    
    public void processAppeal(){
        try{
            
            employeeControllerLocal.processAppeal(appealIdToUpdate, reply, employeeId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Appeal Reply Send Successfully", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while sending reply: " + ex.getMessage(), null));
        
        }
    }

    public Long getAppealIdToUpdate() {
        return appealIdToUpdate;
    }

    public void setAppealIdToUpdate(Long appealIdToUpdate) {
        this.appealIdToUpdate = appealIdToUpdate;
    }

    public Appeal getAppealToUpdate() {
        return appealToUpdate;
    }

    public void setAppealToUpdate(Appeal appealToUpdate) {
        this.appealToUpdate = appealToUpdate;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    
    
    
    
}
