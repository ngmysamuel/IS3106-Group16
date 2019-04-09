/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Type;
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
import stateless.TypeControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named
@ViewScoped
public class TypeManagementManagedBean implements Serializable{

    @EJB(name = "TypeControllerLocal")
    private TypeControllerLocal typeControllerLocal;

    private List<Type> types;
    private List<Type> filteredTypes;
    
    private Type newType;
    
    private Type selectedTypeToView;
    
    private Type selectedTypeToUpdate;
    
    private Type selectedTypeToDelete;
    
    public TypeManagementManagedBean() {
        newType = new Type();
    }
    
    @PostConstruct
    public void postConstruct(){
        types = typeControllerLocal.retrieveAllTypes();
    }
    
    public void viewTypeDetails(ActionEvent event) throws IOException{
        Long typeIdToView = (Long)event.getComponent().getAttributes().get("typeId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("typeIdToView",typeIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewTypeDetails.xhtml");
    }
    
    public void createNewType(){
        try{
            
            Type tp = typeControllerLocal.createNewType(newType);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Type created successfully (Type ID: " + tp.getTypeId()+ ")", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new type: " + ex.getMessage(), null));
        
        }
    }
    
    public void doUpdateType(ActionEvent event){
        
        selectedTypeToUpdate = (Type) event.getComponent().getAttributes().get("typeToUpdate");
        
    }
    
    public void updateType(ActionEvent event){
        try{
            
            typeControllerLocal.updateType(selectedTypeToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Type Updated Successfully", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while updating type: " + ex.getMessage(), null));
        
        }
    }
    
    public void deleteType(ActionEvent event){
        try{
            selectedTypeToDelete = (Type) event.getComponent().getAttributes().get("selectedTypeToDelete");
            
            typeControllerLocal.deleteType(selectedTypeToDelete.getTypeId());
            
            types.remove(selectedTypeToDelete);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Type deleted successfully", null));
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting type: " + ex.getMessage(), null));
            
        }
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public List<Type> getFilteredTypes() {
        return filteredTypes;
    }

    public void setFilteredTypes(List<Type> filteredTypes) {
        this.filteredTypes = filteredTypes;
    }

    public Type getNewType() {
        return newType;
    }

    public void setNewType(Type newType) {
        this.newType = newType;
    }

    public Type getSelectedTypeToView() {
        return selectedTypeToView;
    }

    public void setSelectedTypeToView(Type selectedTypeToView) {
        this.selectedTypeToView = selectedTypeToView;
    }

    public Type getSelectedTypeToUpdate() {
        return selectedTypeToUpdate;
    }

    public void setSelectedTypeToUpdate(Type selectedTypeToUpdate) {
        this.selectedTypeToUpdate = selectedTypeToUpdate;
    }

    public Type getSelectedTypeToDelete() {
        return selectedTypeToDelete;
    }

    public void setSelectedTypeToDelete(Type selectedTypeToDelete) {
        this.selectedTypeToDelete = selectedTypeToDelete;
    }
    
    
    
}
