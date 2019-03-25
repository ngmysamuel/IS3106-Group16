/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Type;
import java.io.IOException;
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
@Named(value = "updateTypeManagedBean")
@ViewScoped
public class UpdateTypeManagedBean {

    @EJB(name = "TypeControllerLocal")
    private TypeControllerLocal typeControllerLocal;
    
    private Long typeIdToUpdate;
    private Type typeToUpdate; 
    
    public UpdateTypeManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        
        typeIdToUpdate = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("typeIdToUpdate");
        
        try{
            
            typeToUpdate = typeControllerLocal.retrieveTypeById(typeIdToUpdate);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the type details: " + ex.getMessage(), null));
        
        }

    }
    
    public void back(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllTypes.xhtml");
    }
    
    public void foo(){
        
    }
    
    public void updateType(){
        try{
            
            typeControllerLocal.updateType(typeToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Type Updated Successfully", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while updating type: " + ex.getMessage(), null));
        
        }
    }

    public Long getTypeIdToUpdate() {
        return typeIdToUpdate;
    }

    public void setTypeIdToUpdate(Long typeIdToUpdate) {
        this.typeIdToUpdate = typeIdToUpdate;
    }

    public Type getTypeToUpdate() {
        return typeToUpdate;
    }

    public void setTypeToUpdate(Type typeToUpdate) {
        this.typeToUpdate = typeToUpdate;
    }
    
    
    
    
    
}
