/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.RegisterNewUser;
import entity.Experience;
import entity.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import stateless.UserControllerLocal;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RegisterUserException;
import util.exception.UpdateUserException;
import util.exception.UserNotFoundException;
import datamodel.ws.rest.RetrieveAllExperiencesExpResrc;
import datamodel.ws.rest.UpdateUser;
import datamodel.ws.rest.UserLogin;
import entity.ExperienceDate;

/**
 *
 * @author samue
 */
@Path("User")
public class UserResource {

    UserControllerLocal userController = lookupUserControllerLocal();

    @Path("getUser/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Long id) {
        try {
            User user = userController.retrieveUserById(id);
            
            for(User u: user.getBlockers()){
                u.getBlocks().clear();
            }
            
            for(User u: user.getBlocks()){
                u.getBlockers().clear();
            }
            
            for(User u: user.getFollowers()){
                u.getFollows().clear();
            }
            
            for(User u: user.getFollows()){
                u.getFollowers().clear();
            }
            
            user.setBookings(null);
            user.setExperienceHosted(null);
            user.setEvaluationsForUserAsGuest(null);
            user.setEvaluationsForUserAsHost(null);
            user.setMessagesReplied(null);
            user.setMessagesSent(null);
            user.setNotifications(null);
            user.getAppeals().clear();
            user.setFollowedExperiences(null);
            return Response.status(Response.Status.OK).entity(user).build();
        } catch (UserNotFoundException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        } catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    @Path("getUserByUsername/{username}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUsername(@PathParam("username") String username) throws UserNotFoundException {
        try {
            User user = userController.retrieveUserByUsername(username);
            
            for(User u: user.getBlockers()){
                u.getBlocks().clear();
            }
            
            for(User u: user.getBlocks()){
                u.getBlockers().clear();
            }
            
            for(User u: user.getFollowers()){
                u.getFollows().clear();
            }
            
            for(User u: user.getFollows()){
                u.getFollowers().clear();
            }
            
            user.setBookings(null);
            user.setExperienceHosted(null);
            user.setEvaluationsForUserAsGuest(null);
            user.setEvaluationsForUserAsHost(null);
            user.setMessagesReplied(null);
            user.setMessagesSent(null);
            user.setNotifications(null);
            user.getAppeals().clear();
            user.setFollowedExperiences(null);
            return Response.status(Response.Status.OK).entity(user).build();
        } catch (UserNotFoundException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        } catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("login")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@QueryParam("username")String username, @QueryParam("password")String password) {
        try {
            User u = userController.login(username, password);
            u.setPassword(null);
            u.setCreditCardDetails(null);
            return Response.status(Response.Status.OK).entity(new UserLogin(u)).build();
        } catch (InvalidLoginCredentialException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorRsp).build();
        } catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("register")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegisterNewUser registerNewUser) {
        User u = new User();
        u.setPassword(registerNewUser.getPassword());
        u.setUsername(registerNewUser.getUsername());
        u.setFirstName(registerNewUser.getFirstName());
        u.setLastName(registerNewUser.getLastName());
        u.setEmail(registerNewUser.getEmail());
        u.setPremium(registerNewUser.getPremium());
        try {
            u = userController.register(u);
            return Response.status(Response.Status.OK).entity(new UserLogin(u)).build();
        } catch (InputDataValidationException | RegisterUserException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorRsp).build();
        } catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    @Path("updateUser")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(UpdateUser updateUser) throws InputDataValidationException, UpdateUserException {
        try{
            User user = userController.updateForRest(updateUser.getUserId(), updateUser.getUsername(), updateUser.getPassword(), updateUser.getEmail(), updateUser.getFirstName(), updateUser.getLastName(), updateUser.getPremium());
            
            for(User u: user.getBlockers()){
                u.getBlocks().clear();
            }
            
            for(User u: user.getBlocks()){
                u.getBlockers().clear();
            }
            
            for(User u: user.getFollowers()){
                u.getFollows().clear();
            }
            
            for(User u: user.getFollows()){
                u.getFollowers().clear();
            }
            
            user.setBookings(null);
            user.setExperienceHosted(null);
            user.setEvaluationsForUserAsGuest(null);
            user.setEvaluationsForUserAsHost(null);
            user.setMessagesReplied(null);
            user.setMessagesSent(null);
            user.setNotifications(null);
            user.getAppeals().clear();
            user.setFollowedExperiences(null);
            
            return Response.status(Response.Status.OK).entity(user).build();
        } catch(InputDataValidationException ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        } catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("followUser")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response followUser (@QueryParam("userId") Long userId, @QueryParam("followId") Long followId) {
        try {
            userController.followUser(userId, followId);
            return Response.status(Response.Status.OK).entity("OK").build();
        } catch (UserNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No user").build();
        } catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
        
    }
    
    @Path("unfollowUser")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollowUser (@QueryParam("userId") Long userId, @QueryParam("unfollowId") Long unfollowId) {
        try {
            userController.unfollowUser(userId,unfollowId);
            return Response.status(Response.Status.OK).entity("OK").build();
        } catch (UserNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No user").build();
        } catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
        
    }
    
    @Path("retrieveAllExperiences")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllExperiences(@QueryParam("username") String username) {
        try {       
            User user = userController.retrieveUserByUsername(username);
            List<Experience> experiences = userController.retrieveAllExperience(user.getUserId());
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
            return Response.status(Response.Status.OK).entity(new RetrieveAllExperiencesExpResrc(experiences)).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("No user").build();
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
