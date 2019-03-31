/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Category;
import java.io.IOException;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.bean.ViewScoped;
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
    private int searchNumOfPeople;

    public IndexManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        categories = categoryControllerLocal.retrieveAllCategories();
    }

    public void searchExperience() throws IOException {
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

    public int getSearchNumOfPeople() {
        return searchNumOfPeople;
    }

    public void setSearchNumOfPeople(int searchNumOfPeople) {
        this.searchNumOfPeople = searchNumOfPeople;

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("searchNumOfPeople", searchNumOfPeople);
    }

}
