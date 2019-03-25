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
@Named(value = "updateCategoryManagedBean")
@ViewScoped
public class UpdateCategoryManagedBean {

    @EJB(name = "CategoryControllerLocal")
    private CategoryControllerLocal categoryControllerLocal;
    
    private Category categoryToUpdate;
    private Long categoryIdToUpdate;
    
    public UpdateCategoryManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        
        categoryIdToUpdate = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("categoryIdToUpdate");
        
        try{
            
            categoryToUpdate = categoryControllerLocal.retrieveCategoryById(categoryIdToUpdate);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the category details: " + ex.getMessage(), null));
        
        }
    }
    
    public void back(ActionEvent event) throws IOException{
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllCategories.xhtml");
        
    }
    
    public void foo(){
        
    }
    
    public void updateLocation(){
        try{
            
            categoryControllerLocal.updateCategory(categoryToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category Updated Successfully", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while updating category: " + ex.getMessage(), null));
        
        }
    }

    public Category getCategoryToUpdate() {
        return categoryToUpdate;
    }

    public void setCategoryToUpdate(Category categoryToUpdate) {
        this.categoryToUpdate = categoryToUpdate;
    }

    public Long getCategoryIdToUpdate() {
        return categoryIdToUpdate;
    }

    public void setCategoryIdToUpdate(Long categoryIdToUpdate) {
        this.categoryIdToUpdate = categoryIdToUpdate;
    }
    
    
    
}
