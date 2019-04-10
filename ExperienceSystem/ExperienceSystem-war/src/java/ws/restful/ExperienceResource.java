/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import com.sun.org.apache.regexp.internal.RE;
import datamodel.ws.rest.CreateNewExperience;
import datamodel.ws.rest.ErrorRsp;
import entity.Experience;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import stateless.ExperienceControllerLocal;
import util.exception.ExperienceNotFoundException;
import datamodel.ws.rest.RetrieveAllExperiencesRsp;
import datamodel.ws.rest.RetrieveExperienceRsp;
import datamodel.ws.rest.UpdateExperience;
import entity.ExperienceDate;
import entity.User;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;
import stateless.CategoryControllerLocal;
import stateless.LanguageControllerLocal;
import stateless.LocationControllerLocal;
import stateless.TypeControllerLocal;
import stateless.UserControllerLocal;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewExperienceException;
import util.exception.DeleteExperienceException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.LanguageNotFoundException;
import util.exception.LocationNotFoundException;
import util.exception.TypeNotFoundException;
import util.exception.UpdateEperienceInfoException;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Path("Experience")
public class ExperienceResource {

    UserControllerLocal userController = lookupUserControllerLocal();

    CategoryControllerLocal categoryController = lookupCategoryControllerLocal();

    LanguageControllerLocal languageController = lookupLanguageControllerLocal();

    LocationControllerLocal locationController = lookupLocationControllerLocal();

    TypeControllerLocal typeController = lookupTypeControllerLocal();

    ExperienceControllerLocal experienceController = lookupExperienceControllerLocal();
    
    @Path("createExperience")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createExperience(CreateNewExperience createNewExperience) throws InputDataValidationException, CreateNewExperienceException{
        
        try{
            Experience newExperience = createNewExperience.getExperienceEntity();

            User host = userController.login(createNewExperience.getUsername(), createNewExperience.getPassword());
            
            newExperience.setHost(host);
            
            Experience exp = experienceController.createNewExperience(newExperience);
            
            exp.getCategory().getExperiences().clear();
                
            exp.getType().getExperiences().clear();

            exp.getLocation().getExperiences().clear();

            exp.getLanguage().getExperiences().clear();

            for(ExperienceDate expDate: exp.getExperienceDates()){
                expDate.setExperience(null);
            }
            for(User u: exp.getFollowers()){
                u.getFollowedExperiences().clear();
            }

            exp.getHost().getExperienceHosted().clear();
            
            return Response.status(Status.OK).entity(new RetrieveExperienceRsp(exp)).build();
        } catch(InvalidLoginCredentialException ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
        } 
        catch(CreateNewExperienceException ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        } catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
        
    }
    
    @Path("updateExperience")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateExperience (UpdateExperience updateExperience) {
        
        try {
            Experience newExperience = updateExperience.getExperienceEntity();
        
            experienceController.updateExperienceInformation(newExperience);
            
            return Response.status(Response.Status.OK).entity(new RetrieveExperienceRsp(newExperience)).build();
        } catch (UpdateEperienceInfoException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        } catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
        
    }
    
    @Path("deleteExperience")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteExperience(@QueryParam("id")Long id) {
        try {
            experienceController.deleteExperience(id);
            return Response.status(Response.Status.OK).entity("ok").build();
        } catch (DeleteExperienceException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        } catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("retrieveAllExperiences")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllExperiences(){
        try{
            List<Experience> experiences = experienceController.retrieveAllExperiences();
            
            for(Experience exp: experiences){
                
                exp.getCategory().getExperiences().clear();
                
                exp.getType().getExperiences().clear();
                
                exp.getLocation().getExperiences().clear();
                
                exp.getLanguage().getExperiences().clear();
                
                for(ExperienceDate expDate: exp.getExperienceDates()){
                    expDate.setExperience(null);
                }
                for(User u: exp.getFollowers()){
                    u.getFollowedExperiences().clear();
                }
                
                exp.getHost().getExperienceHosted().clear();
            }
            
            return Response.status(Status.OK).entity(new RetrieveAllExperiencesRsp(experiences)).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("retrieveHostExperiences")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveHostExperiences(@QueryParam("userId") Long userId){
        try{
            userController.retrieveUserById(userId);
            
            List<Experience> experiences = experienceController.retrieveAllHostExperienceByHostId(userId);
            
            for(Experience exp: experiences){
                
                exp.getCategory().getExperiences().clear();
                
                exp.getType().getExperiences().clear();
                
                exp.getLocation().getExperiences().clear();
                
                exp.getLanguage().getExperiences().clear();
                
                for(ExperienceDate expDate: exp.getExperienceDates()){
                    expDate.setExperience(null);
                }
                for(User u: exp.getFollowers()){
                    u.getFollowedExperiences().clear();
                }
//                List<User> followers = exp.getFollowers();
//                exp.getFollowers().clear();
//                exp.setFollowers(followers);
                
                exp.getHost().getExperienceHosted().clear();
            }
            
            return Response.status(Response.Status.OK).entity(new RetrieveAllExperiencesRsp(experiences)).build();
        }
        catch(UserNotFoundException ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("retrieveExperience/{experienceId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveExperience(@PathParam("experienceId") Long experienceId){
        try{
            Experience exp = experienceController.retrieveExperienceById(experienceId);
            
            exp.getCategory().getExperiences().clear();
                
            exp.getType().getExperiences().clear();
                
            exp.getLocation().getExperiences().clear();
                
            exp.getLanguage().getExperiences().clear();
                
            for(ExperienceDate expDate: exp.getExperienceDates()){
                expDate.setExperience(null);
            }
            for(User u: exp.getFollowers()){
                u.getFollowedExperiences().clear();
            }
            List<User> followers = exp.getFollowers();
            exp.getFollowers().clear();
            exp.setFollowers(followers);
            
            exp.getHost().getExperienceHosted().clear();
            
            return Response.status(Response.Status.OK).entity(new RetrieveExperienceRsp(exp)).build();
        }
        catch(ExperienceNotFoundException ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("followExperience")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response followExperience(@QueryParam("experienceId") Long experienceId,
                            @QueryParam("userId") Long userId){
        try{
            experienceController.addFollowerToExperience(experienceId, userId);
            return Response.status(Response.Status.OK).build();
        }
        catch(UserNotFoundException |  ExperienceNotFoundException ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("unfollowExperience")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollowExperience(@QueryParam("experienceId") Long experienceId,
                            @QueryParam("userId") Long userId){
        try{
            experienceController.removeFollowerFromExperience(experienceId, userId);
            return Response.status(Response.Status.OK).build();
        }
        catch(UserNotFoundException |  ExperienceNotFoundException ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("retrieveFavoriteExperiences")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveFavoriteExperiences(@QueryParam("userId") Long userId){
        try{
            List<Experience> experiences = experienceController.retrieveFavoriteExperiences(userId);
            
            for(Experience exp: experiences){
                
                exp.getCategory().getExperiences().clear();
                
                exp.getType().getExperiences().clear();
                
                exp.getLocation().getExperiences().clear();
                
                exp.getLanguage().getExperiences().clear();
                
                for(ExperienceDate expDate: exp.getExperienceDates()){
                    expDate.setExperience(null);
                }
                for(User u: exp.getFollowers()){
                    u.getFollowedExperiences().clear();
                }
//                List<User> followers = exp.getFollowers();
//                exp.getFollowers().clear();
//                exp.setFollowers(followers);
                
                exp.getHost().getExperienceHosted().clear();
            }
            
            return Response.status(Response.Status.OK).entity(new RetrieveAllExperiencesRsp(experiences)).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("retrievePastExperiences")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePastExperiences(@QueryParam("userId") Long userId){
        try{
            List<Experience> experiences = experienceController.retrievePastExperiences(userId);
            
            for(Experience exp: experiences){
                
                exp.getCategory().getExperiences().clear();
                
                exp.getType().getExperiences().clear();
                
                exp.getLocation().getExperiences().clear();
                
                exp.getLanguage().getExperiences().clear();
                
                for(ExperienceDate expDate: exp.getExperienceDates()){
                    expDate.setExperience(null);
                }
                for(User u: exp.getFollowers()){
                    u.getFollowedExperiences().clear();
                }
                List<User> followers = exp.getFollowers();
                exp.getFollowers().clear();
                exp.setFollowers(followers);
                
                exp.getHost().getExperienceHosted().clear();
            }
            
            return Response.status(Response.Status.OK).entity(new RetrieveAllExperiencesRsp(experiences)).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
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

    private TypeControllerLocal lookupTypeControllerLocal() {
        try {
            Context c = new InitialContext();
            return (TypeControllerLocal) c.lookup("java:global/ExperienceSystem/ExperienceSystem-ejb/TypeController!stateless.TypeControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
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

    private LanguageControllerLocal lookupLanguageControllerLocal() {
        try {
            Context c = new InitialContext();
            return (LanguageControllerLocal) c.lookup("java:global/ExperienceSystem/ExperienceSystem-ejb/LanguageController!stateless.LanguageControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CategoryControllerLocal lookupCategoryControllerLocal() {
        try {
            Context c = new InitialContext();
            return (CategoryControllerLocal) c.lookup("java:global/ExperienceSystem/ExperienceSystem-ejb/CategoryController!stateless.CategoryControllerLocal");
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
