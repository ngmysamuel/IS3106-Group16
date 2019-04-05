/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.RetrieveAllLocationsRsp;
import entity.Experience;
import entity.Location;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import stateless.LocationControllerLocal;

/**
 *
 * @author Asus
 */
@Stateless
@Path("Location")
public class LocationResource {

    private LocationControllerLocal locationController = lookupLocationControllerLocal();
    
    
    
    @Path("retrieveAllLocations")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllLocations(){
        try{
            List<Location> locations = locationController.retrieveAllLocations();
            
            for(Location loc: locations){
                
                for(Experience exp: loc.getExperiences()){
                    exp.setLocation(null);
                }
                
            }
            
            return Response.status(Status.OK).entity(new RetrieveAllLocationsRsp(locations)).build();
        }
        catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    private LocationControllerLocal lookupLocationControllerLocal() {
        try {
            Context c = new InitialContext();
            return (LocationControllerLocal) c.lookup("java:global/ExperienceSystem/ExperienceSystem-ejb/LocationController!stateless.LocationControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
