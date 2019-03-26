/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Type;
import java.io.IOException;
import java.io.Serializable;
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
@Named(value = "deleteTypeManagedBean")
@ViewScoped
public class DeleteTypeManagedBean implements Serializable {

    @EJB(name = "TypeControllerLocal")
    private TypeControllerLocal typeControllerLocal;
    
    private Long typeIdToDelete;
    private Type typeToDelete;
    
    
    public DeleteTypeManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        
        typeIdToDelete = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("typeIdToDelete");
        
        try{
            
            typeToDelete = typeControllerLocal.retrieveTypeById(typeIdToDelete);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the type details: " + ex.getMessage(), null));
        
        }
    }
    
    public void deleteType(){
        
        try{
            
            typeControllerLocal.deleteType(typeIdToDelete);
            
            typeIdToDelete = null;
            typeToDelete = null;
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting type: " + ex.getMessage(), null));
        
        }
    }
    
    public void back(ActionEvent event) throws IOException{
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllTypes.xhtml");
        
    }
    
    public void foo(){
        
    }

    public Long getTypeIdToDelete() {
        return typeIdToDelete;
    }

    public void setTypeIdToDelete(Long typeIdToDelete) {
        this.typeIdToDelete = typeIdToDelete;
    }

    public Type getTypeToDelete() {
        return typeToDelete;
    }

    public void setTypeToDelete(Type typeToDelete) {
        this.typeToDelete = typeToDelete;
    }
    
    
    

}
