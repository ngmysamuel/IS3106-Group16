/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Asus
 */
@Named(value = "indexManagedBean")
@ViewScoped
public class IndexManagedBean implements Serializable{

    /**
     * Creates a new instance of IndexManagedBean
     */
    @EJB
    private UserControllerLocal userController;
    private Date searchDate;
    private int searchNumOfPeople;
    
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
    
    public void searchExperience() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("./userOperations/searchExperience.xhtml");
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("searchDate", searchDate);
    }

    public int getSearchNumOfPeople() {
        return searchNumOfPeople;
    }

    public void setSearchNumOfPeople(int searchNumOfPeople) {
        this.searchNumOfPeople = searchNumOfPeople;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("searchNumOfPeople", searchNumOfPeople);
    }
    
}
