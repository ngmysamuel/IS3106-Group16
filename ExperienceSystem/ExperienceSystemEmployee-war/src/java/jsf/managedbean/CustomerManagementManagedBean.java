/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.User;
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
import stateless.UserControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "customerManagementManagedBean")
@ViewScoped
public class CustomerManagementManagedBean implements Serializable{

    @EJB(name = "UserControllerLocal")
    private UserControllerLocal userControllerLocal;
    
    private List<User> users; 
    
    private User newUser;
    
    private List<User> filteredUsers;
    
    private User selectedUserToUpdate;
    
    private User selectedUserToView;
    
    
    public CustomerManagementManagedBean() {
        newUser = new User();
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
    
    public void doUpdateCustomer(ActionEvent event){
        
        selectedUserToUpdate = (User) event.getComponent().getAttributes().get("userToUpdate");
        
    }
    
    public void updateCustomer(){
        
        try{
            
            userControllerLocal.updatePersonalInformation(selectedUserToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Updated Successfully", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while updating user: " + ex.getMessage(), null));
        
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public List<User> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<User> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    

    public User getSelectedUserToUpdate() {
        return selectedUserToUpdate;
    }

    public void setSelectedUserToUpdate(User selectedUserToUpdate) {
        this.selectedUserToUpdate = selectedUserToUpdate;
    }

    public User getSelectedUserToView() {
        return selectedUserToView;
    }

    public void setSelectedUserToView(User selectedUserToView) {
        this.selectedUserToView = selectedUserToView;
    }
    
}
