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
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.UserControllerLocal;
import util.exception.InputDataValidationException;
import util.exception.UpdateUserException;
import util.exception.UserNotFoundException;

/**
 *
 * @author CaiYuqian
 */
@Named(value = "accountManagementManagedBean")
@ViewScoped
public class AccountManagementManagedBean implements Serializable {

    @EJB
    private UserControllerLocal userController;

    private User currentUser;
    private Long currentUserId;

    public AccountManagementManagedBean() {

    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("******** AccountManagementManagedBean: postConstruct()");
        currentUser = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        currentUserId = currentUser.getUserId();
        System.out.println("    **** currentUser: username = " + currentUser.getUsername());
    }

    public void updateAccountInfo(ActionEvent event) {
        System.out.println("******** AccountManagementManagedBean: updateAccountInfo");

        try {
            userController.update(currentUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account information updated successfully!", null));
        } catch (InputDataValidationException | UpdateUserException ex) {
            // revert back to the orginal user info
            try {
                currentUser = userController.retrieveUserById(currentUserId);
            } catch (UserNotFoundException ex2) {
                // there should not be any exception thrown
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }

    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

}
