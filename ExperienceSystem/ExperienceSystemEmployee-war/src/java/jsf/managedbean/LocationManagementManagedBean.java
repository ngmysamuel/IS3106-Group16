/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Location;
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
import stateless.LocationControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "locationManagementManagedBean")
@ViewScoped
public class LocationManagementManagedBean implements Serializable{

    @EJB
    private LocationControllerLocal locationControllerLocal;
    
    private List<Location> locations;
    
    private List<Location> filteredLocations;
    
    private Location newLocation;
    
    private Location selectedLocationToView;
    private Location selectedLocationToUpdate;
    private Location selectedLocationToDelete;
    
    public LocationManagementManagedBean() {
        newLocation = new Location();
    }
    
    @PostConstruct
    public void postConstruct(){
        locations = locationControllerLocal.retrieveAllLocations();
    }
    
    public void viewLocationDetails(ActionEvent event) throws IOException{
        Long locationIdToView = (Long)event.getComponent().getAttributes().get("locationId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("locationIdToView",locationIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewLocationDetails.xhtml");
    }
    
    public void createNewLocation(){
        try{
            
            Location lc = locationControllerLocal.createNewLocation(newLocation);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Location created successfully (Location ID: " + lc.getLocationId()+ ")", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Location: " + ex.getMessage(), null));
        
        }
    }
    
    public void doUpdateLocation(ActionEvent event){
        
        selectedLocationToUpdate = (Location) event.getComponent().getAttributes().get("locationToUpdate");
        
    }
    
    public void updateLocation(ActionEvent event){
        try{
            
            locationControllerLocal.updateLocation(selectedLocationToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Location Updated Successfully", null));
        
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while updating location: " + ex.getMessage(), null));
        
        }
    }
    
    public void deleteLocation(ActionEvent event){
        try{
            selectedLocationToDelete = (Location) event.getComponent().getAttributes().get("locationToDelete");
            
            locationControllerLocal.deleteLocation(selectedLocationToDelete.getLocationId());
            
            locations.remove(selectedLocationToDelete);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Location deleted successfully", null));
            
        }catch(Exception ex){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting location: " + ex.getMessage(), null));
            
        }
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<Location> getFilteredLocations() {
        return filteredLocations;
    }

    public void setFilteredLocations(List<Location> filteredLocations) {
        this.filteredLocations = filteredLocations;
    }

    public Location getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(Location newLocation) {
        this.newLocation = newLocation;
    }

    public Location getSelectedLocationToView() {
        return selectedLocationToView;
    }

    public void setSelectedLocationToView(Location selectedLocationToView) {
        this.selectedLocationToView = selectedLocationToView;
    }

    public Location getSelectedLocationToUpdate() {
        return selectedLocationToUpdate;
    }

    public void setSelectedLocationToUpdate(Location selectedLocationToUpdate) {
        this.selectedLocationToUpdate = selectedLocationToUpdate;
    }

    public Location getSelectedLocationToDelete() {
        return selectedLocationToDelete;
    }

    public void setSelectedLocationToDelete(Location selectedLocationToDelete) {
        this.selectedLocationToDelete = selectedLocationToDelete;
    }
}
