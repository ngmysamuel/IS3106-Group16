/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Category;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.CategoryControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewAllCategoriesManagedBean")
@RequestScoped
public class ViewAllCategoriesManagedBean {

    @EJB(name = "CategoryControllerLocal")
    private CategoryControllerLocal categoryControllerLocal;

    private List<Category> categories; 
    
    public ViewAllCategoriesManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        categories = categoryControllerLocal.retrieveAllCategories();
    }
    
    public void updateCategory(ActionEvent event) throws IOException{
        Long categoryIdToUpdate = (Long) event.getComponent().getAttributes().get("categoryId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("categoryIdToUpdate", categoryIdToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateCategory.xhtml");
    }
    
    public void deleteCategory(ActionEvent event) throws IOException{
        Long categoryIdToDelete = (Long) event.getComponent().getAttributes().get("categoryId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("categoryIdToDelete", categoryIdToDelete);
        FacesContext.getCurrentInstance().getExternalContext().redirect("deleteCategory.xhtml");
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    
    
}
