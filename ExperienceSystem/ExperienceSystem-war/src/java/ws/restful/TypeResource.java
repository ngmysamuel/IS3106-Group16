/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.RetrieveAllTypesRsp;
import entity.Experience;
import entity.Type;
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
import stateless.TypeControllerLocal;

/**
 *
 * @author Asus
 */

@Stateless
@Path("Type")
public class TypeResource {

    TypeControllerLocal typeController = this.lookupTypeControllerLocal();

    public TypeResource() {
    }
    
    @Path("retrieveAllTypes")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllTypes(){
        try{
            List<Type> types = typeController.retrieveAllTypes();
            
            for(Type type: types){
                for(Experience exp: type.getExperiences()){
                    exp.setType(null);
                }
            }
            
            return Response.status(Status.OK).entity(new RetrieveAllTypesRsp(types)).build();
        }
        catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.OK).entity(errorRsp).build();
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
    
    
    
}
