/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.User;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.UserControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewCustomerDetailsManagedBean")
@ViewScoped
public class ViewCustomerDetailsManagedBean implements Serializable {

    @EJB(name = "UserControllerLocal")
    private UserControllerLocal userControllerLocal;
    
    
    private Long userIdToView;
    private User userToView;
    private String backMode; 
    
    public ViewCustomerDetailsManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        
        userIdToView = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("userIdToView");
        backMode = (String)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("backMode");
        
        try{
            
            userToView = userControllerLocal.retrieveUserById(userIdToView);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the user details: " + ex.getMessage(), null));      
            
        }
    }
    
    
    public void back(ActionEvent event) throws IOException{
        if(backMode == null || backMode.trim().length() == 0){
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllCustomers.xhtml");
        }else{
            FacesContext.getCurrentInstance().getExternalContext().redirect( backMode + ".xhtml");
        }
    }
    
    public void foo(){
        
    }
     
    public void updateCustomerStatus(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("userIdToView", userIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateCustomerStatus.xhtml");
    }

    public Long getUserIdToView() {
        return userIdToView;
    }

    public void setUserIdToView(Long userIdToView) {
        this.userIdToView = userIdToView;
    }

    public User getUserToView() {
        return userToView;
    }

    public void setUserToView(User userToView) {
        this.userToView = userToView;
    }

    public String getBackMode() {
        return backMode;
    }

    public void setBackMode(String backMode) {
        this.backMode = backMode;
    }
    
    
}
