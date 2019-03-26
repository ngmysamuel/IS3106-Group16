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
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.UserControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "searchCustomerByIdManagedBean")
@ViewScoped
public class SearchCustomerByIdManagedBean implements Serializable {

    @EJB(name = "UserControllerLocal")
    private UserControllerLocal userControllerLocal;

    private Long userIdToView;
    private User userToView; 
    
    public SearchCustomerByIdManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct()
    {
        userIdToView = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userIdToView");
        
        try{
            
            userToView = userControllerLocal.retrieveUserById(userIdToView);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred" + ex.getMessage(), null));
        
        }
    }
    
    public void searchCustomer(){
        userIdToView = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userIdToView");
        
        try{
            
            userToView = userControllerLocal.retrieveUserById(userIdToView);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred" + ex.getMessage(), null));
        
        }
    }
    
    public void viewCustomerDetails() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("userIdToView", userIdToView);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("backMode", "searchCustomerById");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewCustomerDetails.xhtml");
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
    
    
    
    
}
