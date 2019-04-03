/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.User;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
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
@Named(value = "viewMyFollowers")
@RequestScoped
public class ViewMyFollowers {

    @EJB
    private UserControllerLocal userController;

    private List<User> followers;
    private List<User> filteredFollowerList;
    
    private User currentUser;

    @PostConstruct
    public void postConstruct() {
    }

    public ViewMyFollowers() {
    }

    public boolean filterByPrice(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        return ((Comparable) value).compareTo(BigDecimal.valueOf(Integer.valueOf(filterText))) > 0;
    }
    
    public void followBack(ActionEvent event) {
        User userToFollow = (User) event.getComponent().getAttributes().get("userToFollow");
        User user = getCurrentUser();
        List<User> ls1 = user.getFollows();
        List<User> ls2 = userToFollow.getFollowers();
        ls1.add(userToFollow);
        ls2.add(user);
        user.setFollows(ls1);
        userToFollow.setFollowers(ls2);
//        userController.update(user);
//        userController.update(userToFollow);
    }
    
    public void block(ActionEvent event) {
        User userToBlock = (User) event.getComponent().getAttributes().get("userToBlock");
        User user = getCurrentUser();
        List<User> ls1 = user.getBlocks();
        List<User> ls2 = userToBlock.getBlockers();
        ls1.add(userToBlock);
        ls2.add(user);
        user.setBlocks(ls1);
        userToBlock.setBlockers(ls2);
//        userController.update(user);
//        userController.update(userToBlock);
    }

    public List<User> getFollowers() {
        User user = getCurrentUser();
        return user.getFollowers();
    }
    
    

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getFilteredFollowerList() {
        return filteredFollowerList;
    }

    public void setFilteredFollowerList(List<User> filteredFollowerList) {
        this.filteredFollowerList = filteredFollowerList;
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
