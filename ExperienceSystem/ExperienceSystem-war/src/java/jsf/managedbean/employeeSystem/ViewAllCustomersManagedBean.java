/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.User;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.UserControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewAllCustomersManagedBean")
@RequestScoped
public class ViewAllCustomersManagedBean {

    @EJB(name = "UserControllerLocal")
    private UserControllerLocal userControllerLocal;

    private List<User> users; 
    
    public ViewAllCustomersManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        users = userControllerLocal.retrieveAllUsers();
    }
    
    public void viewCustomerDetails(ActionEvent event) throws IOException{
        Long userIdToView = (Long) event.getComponent().getAttributes().get("userId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("userIdToView", userIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewCustomerDetails.xhtml");
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    
    
    
    
}
