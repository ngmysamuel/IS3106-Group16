/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.CreateNewExperience;
import entity.Experience;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import stateless.ExperienceControllerLocal;
import util.exception.ExperienceNotFoundException;
import datamodel.ws.rest.RetrieveAllExperiencesExpResrc;
import datamodel.ws.rest.RetrieveListOfExperienceDates;
import datamodel.ws.rest.UpdateExperience;
import entity.ExperienceDate;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import stateless.CategoryControllerLocal;
import stateless.LanguageControllerLocal;
import stateless.LocationControllerLocal;
import stateless.TypeControllerLocal;
import stateless.UserController;
import stateless.UserControllerLocal;
import util.exception.CreateNewExperienceException;
import util.exception.DeleteExperienceException;
import util.exception.InputDataValidationException;
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
    
    
    @Path("getExperienceById/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExperienceById(@PathParam("id") Long id) {
        try {
            return Response.status(Response.Status.OK).entity(experienceController.retrieveExperienceById(id)).build();
        } catch (ExperienceNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Experience not found").build();
        }
    }
    
    @Path("getExperienceByHostId/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExperienceByHostId (@PathParam("id") Long id) {
//        return Response.status(Response.Status.NOT_FOUND).entity("Experience not found").build();
        List<Experience> ls = experienceController.retrieveAllHostExperienceByHostId(id);
        return Response.status(Response.Status.OK).entity(new RetrieveAllExperiencesExpResrc(ls)).build();
    }
    
    @Path("getExperienceDatesFromExperience/{id}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExperienceDatesFromExperience(@PathParam("id")Long id) {
        Experience e;
        try {
            e = experienceController.retrieveExperienceById(id);
        } catch (ExperienceNotFoundException ex) {
            Logger.getLogger(ExperienceResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.NOT_FOUND).entity("Experience not found").build();
        }
        List<ExperienceDate> ls = experienceController.retrieveAllExperienceDates(e);
        RetrieveListOfExperienceDates r = new RetrieveListOfExperienceDates(ls);
        return Response.status(Response.Status.OK).entity(r).build();
    }
    
    @Path("createExperience")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createExperience(CreateNewExperience createNewExperience) throws InputDataValidationException, CreateNewExperienceException{
        Experience e = new Experience();
        e.setAddress(createNewExperience.getAddress());
        e.setTitle(createNewExperience.getTitle());
        e.setCategory(categoryController.retrieveCategoryById(createNewExperience.getCat()));
        e.setType(typeController.retrieveTypeById(createNewExperience.getType()));
        e.setLocation(locationController.retrieveLocationById(createNewExperience.getLocation()));
        e.setLanguage(languageController.retrieveLanguageById(createNewExperience.getLang()));
        try {
            e.setHost(userController.retrieveUserById(createNewExperience.getHost()));
        } catch (UserNotFoundException ex) {
            Logger.getLogger(ExperienceResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.BAD_REQUEST).entity(new CreateNewExperience(e)).build();
        }
        experienceController.createNewExperience(e);
         //catch (CreateNewExperienceException ex) {
//            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("NO").build();
//        } //catch (InputDataValidationException ex) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Input data is off").build();
//        }
        return Response.status(Response.Status.OK).entity(new CreateNewExperience(e)).build();
    }
    
    @Path("updateExperience")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateExperience (UpdateExperience updateExperience) {
        Experience e;
        try {
            e = experienceController.retrieveExperienceById(updateExperience.geteId());
        } catch (ExperienceNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Not found experience").build();
        }
        e.setTitle(updateExperience.getTitle());
        e.setAddress(updateExperience.getAddress());
        e.setActive(updateExperience.getActive());
        e.setCategory(categoryController.retrieveCategoryById(updateExperience.getCat()));
        e.setType(typeController.retrieveTypeById(updateExperience.getType()));
        e.setLocation(locationController.retrieveLocationById(updateExperience.getLocation()));
        e.setLanguage(languageController.retrieveLanguageById(updateExperience.getLang()));
        try {
            experienceController.updateExperienceInformation(e);
        } catch (UpdateEperienceInfoException ex) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("NO").build();
        }
        return Response.status(Response.Status.OK).entity(e).build();
    }
    
    @Path("deleteExperience")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    public Response deleteExperience(@QueryParam("id")Long id) {
        try {
            experienceController.deleteExperience(id);
            return Response.status(Response.Status.OK).entity("ok").build();
        } catch (DeleteExperienceException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("NO").build();
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
