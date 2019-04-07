/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

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

/**
 *
 * @author samue
 */
@Path("User")
public class UserResource {

    UserControllerLocal userController = lookupUserControllerLocal();

    @Path("getUser/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Long id) {
        try {
            return Response.status(Response.Status.OK).entity(userController.retrieveUserById(id)).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }

    @Path("getUserByUsername/{username}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUsername(@PathParam("username") String username) throws UserNotFoundException {
        return Response.status(Response.Status.OK).entity(userController.retrieveUserByUsername(username)).build();
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
            return Response.status(Response.Status.UNAUTHORIZED).entity("Wrong Credentials").build();
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
            return Response.status(Response.Status.UNAUTHORIZED).entity("Wrong input").build();
        }
    }

    @Path("updateUser")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(UpdateUser updateUser) throws InputDataValidationException, UpdateUserException {
        userController.updateForRest(updateUser.getUserId(), updateUser.getUsername(), updateUser.getPassword(), updateUser.getEmail(), updateUser.getFirstName(), updateUser.getLastName(), updateUser.getPremium());
        return Response.status(Response.Status.OK).entity("OK").build();
    }
    
    @Path("unfollowUser/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response unfollowUser (@QueryParam("user") User user, @PathParam("id") Long id) {
        try {
            userController.unfollowUser(user.getUserId(),id);
        } catch (UserNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("No user").build();
        }
        return Response.status(Response.Status.OK).entity("OK").build();
    }
    
    @Path("retrieveAllExperiences")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllExperiences(@QueryParam("username") String username) {
        try {       
            User u = userController.retrieveUserByUsername(username);
            List<Experience> ls = userController.retrieveAllExperience(u.getUserId());
            return Response.status(Response.Status.OK).entity(new RetrieveAllExperiencesExpResrc(ls)).build();
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
