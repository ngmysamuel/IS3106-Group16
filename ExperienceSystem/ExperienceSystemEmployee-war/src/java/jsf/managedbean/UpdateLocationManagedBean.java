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
@Named(value = "updateLocationManagedBean")
@ViewScoped
public class UpdateLocationManagedBean implements Serializable {

    @EJB(name = "LocationControllerLocal")
    private LocationControllerLocal locationControllerLocal;

    private Long locationIdToUpdate;
    private Location locationToUpdate;
    
    public UpdateLocationManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        
        locationIdToUpdate = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("locationIdToUpdate");
        
        try{
            
            locationToUpdate = locationControllerLocal.retrieveLocationById(locationIdToUpdate);
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the type details: " + ex.getMessage(), null));
        
        }
    }
    
    public void back(ActionEvent event) throws IOException{
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllLocations.xhtml");
        
    }
    
    public void foo(){
        
    }
    
    public void updateLocation(){
        try{
            
            locationControllerLocal.updateLocation(locationToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Location Updated Successfully", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while updating location: " + ex.getMessage(), null));
        
        }
    }

    public Long getLocationIdToUpdate() {
        return locationIdToUpdate;
    }

    public void setLocationIdToUpdate(Long locationIdToUpdate) {
        this.locationIdToUpdate = locationIdToUpdate;
    }

    public Location getLocationToUpdate() {
        return locationToUpdate;
    }

    public void setLocationToUpdate(Location locationToUpdate) {
        this.locationToUpdate = locationToUpdate;
    }
    
    
    
}
