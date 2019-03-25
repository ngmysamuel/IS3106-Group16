/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Type;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.TypeControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewAllTypesManagedBean")
@RequestScoped
public class ViewAllTypesManagedBean {

    @EJB(name = "TypeControllerLocal")
    private TypeControllerLocal typeControllerLocal;

    private List<Type> types; 
    
    public ViewAllTypesManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        
        types = typeControllerLocal.retrieveAllTypes();
        
    }
    
    public void updateType(ActionEvent event) throws IOException{
        
        Long typeIdToUpdate = (Long) event.getComponent().getAttributes().get("typeId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("typeIdToUpdate", typeIdToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateType.xhtml");
        
    }
    
    public void deleteType(ActionEvent event) throws IOException{
        Long typeIdToDelete = (Long) event.getComponent().getAttributes().get("typeId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("typeIdToDelete", typeIdToDelete);
        FacesContext.getCurrentInstance().getExternalContext().redirect("deleteTypes.xhtml");
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }
    
    
    
    
}
