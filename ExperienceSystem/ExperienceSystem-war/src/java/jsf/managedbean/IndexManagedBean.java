/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import stateless.UserControllerLocal;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Named(value = "indexManagedBean")
@RequestScoped
public class IndexManagedBean {

    @EJB
    private UserControllerLocal userController;
    private List<Integer> searchDate;
    private List<Integer> searchNumOfPeople;
    
    public IndexManagedBean() {
    }
    
    @PostConstruct
    public void kk() {
        User u = new User();
        try {
            u = userController.retrieveUserByUsername("John");
        } catch (UserNotFoundException ex) {
            Logger.getLogger(IndexManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
System.out.println(u.getUsername());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUser", u);
    }

    public List<Integer> getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(List<Integer> searchDate) {
        this.searchDate = searchDate;
    }

    public List<Integer> getSearchNumOfPeople() {
        return searchNumOfPeople;
    }

    public void setSearchNumOfPeople(List<Integer> searchNumOfPeople) {
        this.searchNumOfPeople = searchNumOfPeople;
    }
    
    
}
