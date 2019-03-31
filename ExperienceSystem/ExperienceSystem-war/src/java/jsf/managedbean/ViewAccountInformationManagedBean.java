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
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.UserControllerLocal;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Named(value = "viewAccountInformationManagedBean")
@ViewScoped
public class ViewAccountInformationManagedBean implements Serializable {

    @EJB
    private UserControllerLocal userController;

    private User currentUser;
    
    public ViewAccountInformationManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        System.out.println("******** ViewAccountInformationManagedBean: postConstruct()");
        User currentUser = (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        System.out.println("    **** currentUser: username = " + currentUser.getUsername());
        System.out.println("    **** currentUser: username = " + currentUser.getUsername());
    }
    
    public void updateAccountInfo(ActionEvent event) {
        System.out.println("******** ViewAccountInformationManagedBean: updateAccountInfo");
        System.out.println("    **** currentUser: username = " + currentUser.getUsername());
        System.out.println("    **** currentUser: firstName = " + currentUser.getFirstName());
        System.out.println("    **** currentUser: lastName = " + currentUser.getLastName());
        userController.update(currentUser);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    
}
