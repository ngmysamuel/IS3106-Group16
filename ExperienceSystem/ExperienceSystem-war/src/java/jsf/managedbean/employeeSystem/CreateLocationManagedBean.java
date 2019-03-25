/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Location;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.LocationControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "createLocationManagedBean")
@ViewScoped
public class CreateLocationManagedBean {

    @EJB(name = "LocationControllerLocal")
    private LocationControllerLocal locationControllerLocal;

    private Location newLocation;
    private Long locationId; 
    
    public CreateLocationManagedBean() {
    }
    
    public void createNewLoction(){
        try{
            
            Location lt = locationControllerLocal.createNewLocation(newLocation);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Location created successfully (Location ID: " + lt.getLocationId()+ ")", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Location: " + ex.getMessage(), null));
        
        }
    }

    public Location getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(Location newLocation) {
        this.newLocation = newLocation;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
    
    
    
    
}
