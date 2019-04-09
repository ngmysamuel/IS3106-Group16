/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.CreateNewLanguage;
import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.RetrieveAllLanguages;
import entity.Experience;
import entity.Language;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import stateless.LanguageControllerLocal;
import util.exception.CreateNewLanguageException;
import util.exception.DeleteLanguageException;
import util.exception.InputDataValidationException;
import util.exception.LanguageNotFoundException;

/**
 *
 * @author samue
 */
@Path("Language")
public class LanguageResource {

    LanguageControllerLocal languageController = lookupLanguageControllerLocal();
    
    @Path("getAllLanguages")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLanguages() {
        try{
            List<Language> ls = languageController.retrieveAllLanguages();

            for(Language l: ls){
                for(Experience e: l.getExperiences()){
                    e.setLanguage(null);
                }
            }

            RetrieveAllLanguages r = new RetrieveAllLanguages(ls);
            return Response.status(Response.Status.OK).entity(r).build();
        } catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("getLanguageById/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLanguageById(@PathParam("id")Long id) {
        try{
            Language l = languageController.retrieveLanguageById(id);

            if (l == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("No language").build();
            }
            for(Experience e: l.getExperiences()){
                e.setLanguage(null);
            }

            return Response.status(Response.Status.OK).entity(l).build();
        } catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
//    @Path("createLanguage")
//    @PUT
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response createLanguage(CreateNewLanguage createNewLanguage) {
//        Language l = new Language();
//        l.setName(createNewLanguage.getName());
//        try {
//            languageController.createNewLanguage(l);
//            return Response.status(Response.Status.OK).entity(new CreateNewLanguage(l.getName())).build();
//        } catch (CreateNewLanguageException ex) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Excpetion gen").build();
//        } catch (InputDataValidationException ex) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Input data validation failed").build();
//        }
//    }
//    
//    @Path("deleteLanguage/{id}")
//    @DELETE
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response deleteLanguage(@PathParam("id")Long id) {
//        try {
//            languageController.deleteLanguage(id);
//        } catch (LanguageNotFoundException ex) {
//            return Response.status(Response.Status.NOT_FOUND).entity("Language not found").build();
//        } catch (DeleteLanguageException ex) {
//            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Excpetion").build();
//        }
//        return Response.status(Response.Status.OK).entity("Success").build();
//    }
    
//    @DELETE
//    @Consumes(MediaType.APPLICATION_JSON)

    private LanguageControllerLocal lookupLanguageControllerLocal() {
        try {
            Context c = new InitialContext();
            return (LanguageControllerLocal) c.lookup("java:global/ExperienceSystem/ExperienceSystem-ejb/LanguageController!stateless.LanguageControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
