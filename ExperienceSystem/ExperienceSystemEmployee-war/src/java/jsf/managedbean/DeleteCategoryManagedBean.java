/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Category;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.CategoryControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "deleteCategoryManagedBean")
@ViewScoped
public class DeleteCategoryManagedBean {

    @EJB(name = "CategoryControllerLocal")
    private CategoryControllerLocal categoryControllerLocal;
    
    private Long categoryIdToDelete;
    private Category categoryToDelete;
    
    public DeleteCategoryManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        
        categoryIdToDelete = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("categoryToDelete");
        
        try{
            
            categoryToDelete = categoryControllerLocal.retrieveCategoryById(categoryIdToDelete);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the category details: " + ex.getMessage(), null));
        
        }
        
    }
    
    public void deleteLocation(){
        try{
            categoryControllerLocal.deleteCategory(categoryIdToDelete);
            
            categoryIdToDelete = null;
            categoryToDelete = null;
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting category: " + ex.getMessage(), null));
            
        }
        
    }
    
    public void back(ActionEvent event) throws IOException{
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllCategories.xhtml");
        
    }
    
    public void foo(){
        
    }

    public Long getCategoryIdToDelete() {
        return categoryIdToDelete;
    }

    public void setCategoryIdToDelete(Long categoryIdToDelete) {
        this.categoryIdToDelete = categoryIdToDelete;
    }

    public Category getCategoryToDelete() {
        return categoryToDelete;
    }

    public void setCategoryToDelete(Category categoryToDelete) {
        this.categoryToDelete = categoryToDelete;
    }
    
    
}
