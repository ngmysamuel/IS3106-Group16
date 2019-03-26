/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.User;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.UserControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "updateCustomerStatusManagedBean")
@ViewScoped
public class UpdateCustomerStatusManagedBean implements Serializable {

    @EJB(name = "UserControllerLocal")
    private UserControllerLocal userControllerLocal;
    
    private Long userIdToUpdate;
    private User userToUpdate;
            
    
    public UpdateCustomerStatusManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        
        userIdToUpdate = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("userIdToUpdate");
        
        try{
            
            userToUpdate = userControllerLocal.retrieveUserById(userIdToUpdate);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the user details: " + ex.getMessage(), null));
        
        }
    }
    
    public void foo(){
        
    }
    
    public void updateLanguage(){
        try{
            
            userControllerLocal.updatePersonalInformation(userToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Updated Successfully", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while updating user: " + ex.getMessage(), null));
        
        }
    }

    public Long getUserIdToUpdate() {
        return userIdToUpdate;
    }

    public void setUserIdToUpdate(Long userIdToUpdate) {
        this.userIdToUpdate = userIdToUpdate;
    }

    public User getUserToUpdate() {
        return userToUpdate;
    }

    public void setUserToUpdate(User userToUpdate) {
        this.userToUpdate = userToUpdate;
    }
    
    
}
