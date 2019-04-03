/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.User;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import stateless.UserControllerLocal;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Asus
 */
@Named(value = "userLoginManagedBean")
@RequestScoped
public class UserLoginManagedBean {

    @EJB
    private UserControllerLocal userController;
    
    private String username;
    private String password;

    public UserLoginManagedBean() {
    }
    
    public void login(ActionEvent event) throws IOException {
        System.out.println("******** UserLoginManagedBean : login() -> username: " + username + " password: " + password);
        try{
            User user = userController.login(username, password);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUser", user);
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
        } catch(InvalidLoginCredentialException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential: " + ex.getMessage(), null));
        }
    }
    
    public void logout(ActionEvent event) throws IOException {
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
      
}
