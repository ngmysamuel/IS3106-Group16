/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Category;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.CategoryControllerLocal;
import stateless.UserControllerLocal;

/**
 *
 * @author Asus
 */
@Named(value = "indexManagedBean")
@ViewScoped
public class IndexManagedBean implements Serializable {

    @EJB
    private UserControllerLocal userControllerLocal;
    @EJB
    private CategoryControllerLocal categoryControllerLocal;
    
    private List<Category> categories;
    private Date minSearchDate;
    
    // User input
    private Date searchDate;
    private Long searchCategoryId;
    private Integer searchNumOfPeople;

    public IndexManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        categories = categoryControllerLocal.retrieveAllCategories();
    }

    public void searchExperience(ActionEvent event) throws IOException {
        System.out.println("******** IndexManagedBean: searchExperience");
        System.out.println("    **** IndexManagedBean: searchDate" + searchDate.toString());
        System.out.println("    **** IndexManagedBean: searchCategoryId " + searchCategoryId);
        System.out.println("    **** IndexManagedBean: searchNumOfPeople " + searchNumOfPeople);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("searchDate", searchDate);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("searchCategoryId", searchCategoryId);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("searchNumOfPeople", searchNumOfPeople);
        FacesContext.getCurrentInstance().getExternalContext().redirect("./userOperations/searchExperience.xhtml");
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Date getMinSearchDate() {
        minSearchDate = new Date();
        return minSearchDate;
    }

    public void setMinSearchDate(Date minSearchDate) {
        this.minSearchDate = minSearchDate;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }

    public Long getSearchCategoryId() {
        return searchCategoryId;
    }

    public void setSearchCategoryId(Long searchCategoryId) {
        this.searchCategoryId = searchCategoryId;
    }

    public Integer getSearchNumOfPeople() {
        return searchNumOfPeople;
    }

    public void setSearchNumOfPeople(Integer searchNumOfPeople) {
        this.searchNumOfPeople = searchNumOfPeople;
    }

}
