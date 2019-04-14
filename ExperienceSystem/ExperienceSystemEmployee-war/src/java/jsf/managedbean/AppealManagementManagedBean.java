/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Appeal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.AppealControllerLocal;
import stateless.EmployeeControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "appealManagementManagedBean")
@ViewScoped
public class AppealManagementManagedBean implements Serializable{

    @EJB(name = "AppealControllerLocal")
    private AppealControllerLocal appealControllerLocal;

    @EJB(name = "EmployeeControllerLocal")
    private EmployeeControllerLocal employeeControllerLocal;
    
    
    private List<Appeal> appeals;
    private List<Appeal> filteredAppeals;
    
    private Appeal selectedAppealToView;
    
    private Appeal selectedAppealToUpdate;
    
    private String replyMessage;
    
    private Long employeeId;
    public AppealManagementManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        appeals = appealControllerLocal.retrieveAllAppeals();
    }
    
    public void viewAppealDetails(ActionEvent event) throws IOException{
        Long appealIdToView = (Long) event.getComponent().getAttributes().get("appealId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("appealIdToView",appealIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAppealDetails.xhtml");
    }
    
    public void doUpdateAppeal(ActionEvent event){
        
        selectedAppealToUpdate = (Appeal) event.getComponent().getAttributes().get("appealToUpdate");
        replyMessage = (String) event.getComponent().getAttributes().get("replyMessage");
        employeeId = (Long) event.getComponent().getAttributes().get("employeeId");
    }
    
    public void updateCategory(ActionEvent event){
        try{
            
            appealControllerLocal.processAppeal(selectedAppealToUpdate.getAppealId(), replyMessage, employeeId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Appeal Reply Successfully", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while replying appeal: " + ex.getMessage(), null));
        
        }
    }

    public List<Appeal> getAppeals() {
        return appeals;
    }

    public void setAppeals(List<Appeal> appeals) {
        this.appeals = appeals;
    }

    public List<Appeal> getFilteredAppeals() {
        return filteredAppeals;
    }

    public void setFilteredAppeals(List<Appeal> filteredAppeals) {
        this.filteredAppeals = filteredAppeals;
    }

    public Appeal getSelectedAppealToView() {
        return selectedAppealToView;
    }

    public void setSelectedAppealToView(Appeal selectedAppealToView) {
        this.selectedAppealToView = selectedAppealToView;
    }

    public Appeal getSelectedAppealToUpdate() {
        return selectedAppealToUpdate;
    }

    public void setSelectedAppealToUpdate(Appeal selectedAppealToUpdate) {
        this.selectedAppealToUpdate = selectedAppealToUpdate;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    
    
    
    
}
