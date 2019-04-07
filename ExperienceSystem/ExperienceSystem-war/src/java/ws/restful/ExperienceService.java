/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.RetrieveAllExperiencesRsp;
import datamodel.ws.rest.RetrieveExperienceRsp;
import entity.Experience;
import entity.ExperienceDate;
import entity.User;
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
import util.exception.ExperienceNotFoundException;
import util.exception.UserNotFoundException;

/**
 *
 * @author Asus
 */
@Stateless
@Path("Experience")
public class ExperienceService {

    ExperienceControllerLocal experienceController = lookupExperienceControllerLocal();
    
    
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
                
                exp.getHost().getExperienceHosted().clear();
            }
            
            return Response.status(Status.OK).entity(new RetrieveAllExperiencesRsp(experiences)).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
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
            
            return Response.status(Status.OK).entity(new RetrieveExperienceRsp(exp)).build();
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
    
    @Path("followExperience")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response followExperience(@QueryParam("experienceId") Long experienceId,
                            @QueryParam("userId") Long userId){
        try{
            experienceController.addFollowerToExperience(experienceId, userId);
            return Response.status(Status.OK).build();
        }
        catch(UserNotFoundException |  ExperienceNotFoundException ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("unfollowExperience")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollowExperience(@QueryParam("experienceId") Long experienceId,
                            @QueryParam("userId") Long userId){
        try{
            experienceController.removeFollowerFromExperience(experienceId, userId);
            return Response.status(Status.OK).build();
        }
        catch(UserNotFoundException |  ExperienceNotFoundException ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
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
                List<User> followers = exp.getFollowers();
                exp.getFollowers().clear();
                exp.setFollowers(followers);
                
                exp.getHost().getExperienceHosted().clear();
            }
            
            return Response.status(Status.OK).entity(new RetrieveAllExperiencesRsp(experiences)).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
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
            
            return Response.status(Status.OK).entity(new RetrieveAllExperiencesRsp(experiences)).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
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
