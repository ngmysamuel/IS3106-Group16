/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Location;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import stateless.LocationControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewAllLocationsManagedBean")
@RequestScoped
public class ViewAllLocationsManagedBean {

    @EJB(name = "LocationControllerLocal")
    private LocationControllerLocal locationControllerLocal;

    private List<Location> locations;
    
    public ViewAllLocationsManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        locations = locationControllerLocal.retrieveAllLocations();
    }
    
    public void updateLocation(ActionEvent event) throws IOException{
        Long locationToUpdate = (Long) event.getComponent().getAttributes().get("locationId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("locationToUpdate", locationToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateLocation.xhtml");
    }
    
    public void deleteLocation(ActionEvent event) throws IOException{
        Long locationToDelete = (Long) event.getComponent().getAttributes().get("locationId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("locationToDelete", locationToDelete);
        FacesContext.getCurrentInstance().getExternalContext().redirect("deleteLocation.xhtml");
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
    
}
