/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Category;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
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
@Named(value = "categoryManagementManagedBean")
@ViewScoped
public class CategoryManagementManagedBean implements Serializable{

    @EJB(name = "CategoryControllerLocal")
    private CategoryControllerLocal categoryControllerLocal;

    private List<Category> categories;
    
    private List<Category> filteredCategories;
    
    private Category newCategory;
    
    private Category selectedCategoryToView;
    
    private Category selectedCategoryToUpdate;
    
    private Category selectedCategoryToDelete;
    
    public CategoryManagementManagedBean() {
        newCategory = new Category();
    }
    
    @PostConstruct
    public void postConstruct(){
        categories = categoryControllerLocal.retrieveAllCategories();
    }
    
    public void viewCategoryDetails(ActionEvent event) throws IOException{
        Long categoryIdToView = (Long)event.getComponent().getAttributes().get("categoryId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("categoryIdToView",categoryIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewCategoryDetails.xhtml");
    }
    
    public void createNewCategory(){
        try{
            
            Category cg = categoryControllerLocal.createNewCategory(newCategory);
            categories.add(cg);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Category created successfully (Category ID: " + cg.getCategoryId()+ ")", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Category: " + ex.getMessage(), null));
        
        }
    }
    
    public void doUpdateCategory(ActionEvent event){
        
        selectedCategoryToUpdate = (Category) event.getComponent().getAttributes().get("categoryToUpdate");
        
    }
    
    public void updateCategory(ActionEvent event){
        try{
            
            categoryControllerLocal.updateCategory(selectedCategoryToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category Updated Successfully", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while updating category: " + ex.getMessage(), null));
        
        }
    }
    
    public void deleteCategory(ActionEvent event){
        try{
            selectedCategoryToDelete = (Category) event.getComponent().getAttributes().get("categoryToDelete");
            
            categoryControllerLocal.deleteCategory(selectedCategoryToDelete.getCategoryId());
            
            categories.remove(selectedCategoryToDelete);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category deleted successfully", null));
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting category: " + ex.getMessage(), null));
            
        }
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getFilteredCategories() {
        return filteredCategories;
    }

    public void setFilteredCategories(List<Category> filteredCategories) {
        this.filteredCategories = filteredCategories;
    }

    

    public Category getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(Category newCategory) {
        this.newCategory = newCategory;
    }

    public Category getSelectedCategoryToView() {
        return selectedCategoryToView;
    }

    public void setSelectedCategoryToView(Category selectedCategoryToView) {
        this.selectedCategoryToView = selectedCategoryToView;
    }

    public Category getSelectedCategoryToUpdate() {
        return selectedCategoryToUpdate;
    }

    public void setSelectedCategoryToUpdate(Category selectedCategoryToUpdate) {
        this.selectedCategoryToUpdate = selectedCategoryToUpdate;
    }

    public Category getSelectedCategoryToDelete() {
        return selectedCategoryToDelete;
    }

    public void setSelectedCategoryToDelete(Category selectedCategoryToDelete) {
        this.selectedCategoryToDelete = selectedCategoryToDelete;
    }
    
    
    
}
