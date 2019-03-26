/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.User;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.UserControllerLocal;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Named(value = "viewPersonalInformation")
@RequestScoped
public class ViewPersonalInformation {

    @EJB
    private UserControllerLocal userController;

    private User currentUser;
    
    public ViewPersonalInformation() {
    }
    
    public void update(ActionEvent e) {
        userController.update(currentUser);
    }

    public User getCurrentUser() {
        User u = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustEntity");
        try {
            u = userController.retrieveUserByUsername(u.getUsername());
        } catch (UserNotFoundException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "wrong", null));
        }
        return u;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    
}
