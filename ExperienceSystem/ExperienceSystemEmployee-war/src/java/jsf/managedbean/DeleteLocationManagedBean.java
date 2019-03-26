/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Location;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.LocationControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "deleteLocationManagedBean")
@ViewScoped
public class DeleteLocationManagedBean implements Serializable {

    @EJB(name = "LocationControllerLocal")
    private LocationControllerLocal locationControllerLocal;

    private Long locationIdToDelete;
    private Location locationToDelete;
    
    public DeleteLocationManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        
        locationIdToDelete = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("locationIdToDelete");
        
        try{
            
            locationToDelete = locationControllerLocal.retrieveLocationById(locationIdToDelete);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the location details: " + ex.getMessage(), null));
        
        }
        
    }
    
    public void deleteLocation(){
        try{
            locationControllerLocal.deleteLocation(locationIdToDelete);
            
            locationIdToDelete = null;
            locationToDelete = null;
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting location: " + ex.getMessage(), null));
        }
        
    }
    
    public void back(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllLocations.xhtml");
    }
    
    public void foo(){
        
    }

    public Long getLocationIdToDelete() {
        return locationIdToDelete;
    }

    public void setLocationIdToDelete(Long locationIdToDelete) {
        this.locationIdToDelete = locationIdToDelete;
    }

    public Location getLocationToDelete() {
        return locationToDelete;
    }

    public void setLocationToDelete(Location locationToDelete) {
        this.locationToDelete = locationToDelete;
    }
    
    
}
