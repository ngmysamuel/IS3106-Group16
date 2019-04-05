/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.RetrieveAllCategoriesRsp;
import entity.Category;
import entity.Experience;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
import stateless.CategoryControllerLocal;

/**
 *
 * @author Asus
 */
@Stateless
@Path("Category")
public class CategoryResource{

    private CategoryControllerLocal categoryController = this.lookupCategoryControllerLocal();
    
    public CategoryResource() {
    }
    
    @Path("retrieveAllCategories")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCategories(){
        try {
            List<Category> categories = categoryController.retrieveAllCategories();
            
            for(Category cat: categories){
                for(Experience exp: cat.getExperiences()){
                    exp.setCategory(null);
                }
            }
            
            return Response.status(Status.OK).entity(new RetrieveAllCategoriesRsp(categories)).build();
        }
        catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
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

}
