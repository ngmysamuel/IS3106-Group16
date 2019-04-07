/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.RetrieveAllExperienceDatesRsp;
import datamodel.ws.rest.RetrieveExperienceDateRsp;
import entity.Booking;
import entity.Experience;
import entity.ExperienceDate;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;
import util.exception.CreateNewExperienceDateException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateEperienceInfoException;

/**
 *
 * @author Asus
 */
@Stateless
@Path("ExperienceDate")
public class ExperienceDateService {

    ExperienceControllerLocal experienceController = lookupExperienceControllerLocal();

    ExperienceDateControllerLocal experienceDateController = lookupExperienceDateControllerLocal();
    
    @Path("retrieveExperienceDates/{experienceId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveExperienceDates(@PathParam("experienceId") Long experienceId){
        try{
            Experience experience = experienceController.retrieveExperienceById(experienceId);
//            
//            List<ExperienceDate> experienceDates = experienceController.retrieveAllExperienceDates(experience);

            List<ExperienceDate> experienceDates = experienceDateController.retrieveExperienceDatesOfAnExperience(experience);
            
            for(ExperienceDate ed: experienceDates){
                for(Booking b: ed.getBookings()){
                    b.setExperienceDate(null);
                }
                
                ed.getExperience().getExperienceDates().clear();
                
                if(ed.getExperienceDateCancellationReport() != null){
                    ed.getExperienceDateCancellationReport().setExperienceDate(null);
                }
                
                if(ed.getExperienceDatePaymentReport() != null){
                    ed.getExperienceDatePaymentReport().setExperienceDate(null);
                }
                
            }
            
            return Response.status(Status.OK).entity(new RetrieveAllExperienceDatesRsp(experienceDates)).build();
//            return Response.status(Status.OK).build();
        }
        catch(ExperienceNotFoundException ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("CreateExperienceDate")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createExperienceDate(@QueryParam("experienceId") Long experienceId,
                                            @QueryParam("startDate") Date startDate,
                                            @QueryParam("capacity") Integer capacity,
                                            @QueryParam("price") BigDecimal price)
    {
        try {
            ExperienceDate ed = new ExperienceDate();
            Experience ex = experienceController.retrieveExperienceById(experienceId);
            ed.setExperience(ex);
            ed.setCapacity(capacity);
            ed.setSpotsAvailable(capacity);
            ed.setStartDate(startDate);
            experienceDateController.createNewExperienceDate(ed);
            ex.getExperienceDates().add(ed);
            experienceController.updateExperienceInformation(ex);
            
            return Response.status(Status.OK).entity(new RetrieveExperienceDateRsp(ed)).build();
        } catch (CreateNewExperienceDateException | InputDataValidationException | UpdateEperienceInfoException | ExperienceNotFoundException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        } catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    private ExperienceDateControllerLocal lookupExperienceDateControllerLocal() {
        try {
            Context c = new InitialContext();
            return (ExperienceDateControllerLocal) c.lookup("java:global/ExperienceSystem/ExperienceSystem-ejb/ExperienceDateController!stateless.ExperienceDateControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private ExperienceControllerLocal lookupExperienceControllerLocal() {
        try {
            Context c = new InitialContext();
            return (ExperienceControllerLocal) c.lookup("java:global/ExperienceSystem/ExperienceSystem-ejb/ExperienceController!stateless.ExperienceControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    
}
