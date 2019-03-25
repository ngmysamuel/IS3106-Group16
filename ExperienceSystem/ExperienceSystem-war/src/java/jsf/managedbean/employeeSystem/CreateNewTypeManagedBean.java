/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Type;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.TypeControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "createNewTypeManagedBean")
@RequestScoped
public class CreateNewTypeManagedBean {

    @EJB(name = "TypeControllerLocal")
    private TypeControllerLocal typeControllerLocal;

    private Type newType;
    private Long typeId;
    
    public CreateNewTypeManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        
    }
    
    public void createNewType(){
        try{
            
            Type tp = typeControllerLocal.createNewType(newType);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Type created successfully (Type ID: " + tp.getTypeId()+ ")", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Type: " + ex.getMessage(), null));
        
        }
    }

    public Type getNewType() {
        return newType;
    }

    public void setNewType(Type newType) {
        this.newType = newType;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
    
    
}
