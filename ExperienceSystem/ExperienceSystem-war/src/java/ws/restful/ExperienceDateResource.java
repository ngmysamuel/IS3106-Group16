/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.CreateExperienceDate;
import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.RetrieveAllExperienceDatesRsp;
import datamodel.ws.rest.RetrieveExperienceDateRsp;
import entity.Booking;
import entity.Experience;
import entity.ExperienceDate;
import entity.User;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;
import stateless.UserControllerLocal;
import util.exception.CreateNewExperienceDateException;
import util.exception.ExperienceDateNotActiveException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UpdateEperienceInfoException;

/**
 *
 * @author Asus
 */
@Stateless
@Path("ExperienceDate")
public class ExperienceDateResource {

    private UserControllerLocal userController = lookupUserControllerLocal();

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
    
    @Path("retrieveActiveExperienceDates/{experienceId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveActiveExperienceDates(@PathParam("experienceId") Long experienceId){
        try{
            Experience experience = experienceController.retrieveExperienceById(experienceId);
//            
//            List<ExperienceDate> experienceDates = experienceController.retrieveAllExperienceDates(experience);

            List<ExperienceDate> experienceDates = experienceDateController.retrieveAllActiveExperienceDatesByExperienceId(experienceId);
            
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
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createExperienceDate(CreateExperienceDate newExperienceDate)
    {
        try {
            User u = userController.login(newExperienceDate.getUsername(), newExperienceDate.getPassword());
            
            Experience experience = experienceController.retrieveExperienceById(newExperienceDate.getExperienceId());
            if(!u.equals(experience.getHost())){
                ErrorRsp errorRsp = new ErrorRsp("You are not the host of this experience");
                return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
            }
            
            newExperienceDate.getExperienceDateEntity().setExperience(experience);
            
            ExperienceDate experienceDate = experienceDateController.createNewExperienceDate(newExperienceDate.getExperienceDateEntity());
            
            return Response.status(Status.OK).entity(experienceDate).build();
        } catch(InvalidLoginCredentialException ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
        } catch (CreateNewExperienceDateException | InputDataValidationException | ExperienceNotFoundException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        } catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("DeleteExperienceDate")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteExperienceDate(@QueryParam("experienceDateId") Long experienceDateId,
                                            @QueryParam("reason") String reason){
        try{
            experienceDateController.deleteExperienceDate(experienceDateId, reason);
            
            return Response.status(Status.OK).build();
        } catch (ExperienceDateNotActiveException ex) {
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
    
    private UserControllerLocal lookupUserControllerLocal() {
        try {
            Context c = new InitialContext();
            return (UserControllerLocal) c.lookup("java:global/ExperienceSystem/ExperienceSystem-ejb/UserController!stateless.UserControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    
}
