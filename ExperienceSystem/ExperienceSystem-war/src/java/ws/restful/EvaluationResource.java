/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.CreateNewEval;
import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.RetrieveAllEvaluations;
import entity.Evaluation;
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
import stateless.BookingControllerLocal;
import stateless.EvaluationControllerLocal;
import stateless.UserControllerLocal;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Path("Evaluation")
public class EvaluationResource {

    UserControllerLocal userController = lookupUserControllerLocal();

    BookingControllerLocal bookingController = lookupBookingControllerLocal();

    EvaluationControllerLocal evaluationController = lookupEvaluationControllerLocal();
    
    @Path("getAllEval")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEvaluations() {
        try{
            List<Evaluation> ls = evaluationController.retrieveAllEvaluations();
            
            for(Evaluation e: ls){
                e.getBooking().setEvaluationByGuest(null);
                
                e.getBooking().setEvaluationByHost(null);
                
                e.getUserEvaluating().getEvaluationsForUserAsGuest().clear();
                
                e.getUserEvaluating().getEvaluationsForUserAsHost().clear();
            }
            
            RetrieveAllEvaluations r = new RetrieveAllEvaluations(ls);
            return Response.status(Response.Status.OK).entity(r).build();
        } catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("getEvalById/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvaluationById(@PathParam("id")Long id) {
        try{
            Evaluation e = evaluationController.retrieveEvaluationById(id);
            
            if(e == null){
                return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorRsp("Evaluation not found!")).build();
            }
            
            e.getBooking().setEvaluationByGuest(null);
                
            e.getBooking().setEvaluationByHost(null);

            e.getUserEvaluating().getEvaluationsForUserAsGuest().clear();

            e.getUserEvaluating().getEvaluationsForUserAsHost().clear();
            
            return Response.status(Response.Status.OK).entity(e).build();
        } catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("getEvalByBookingId/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvaluationByBookingId(@PathParam("id")Long id) {
        try{
            
            List<Evaluation> ls = evaluationController.retrieveEvaluationsByBookingId(id);
            
            for(Evaluation e: ls){
                e.getBooking().setEvaluationByGuest(null);
                
                e.getBooking().setEvaluationByHost(null);
                
                e.getUserEvaluating().getEvaluationsForUserAsGuest().clear();
                
                e.getUserEvaluating().getEvaluationsForUserAsHost().clear();
            }
            
            RetrieveAllEvaluations r = new RetrieveAllEvaluations(ls);
            return Response.status(Response.Status.OK).entity(r).build();
        } catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("createEval")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEvaluation(CreateNewEval createNewEval) throws UserNotFoundException {
        try{
            Evaluation l = new Evaluation();
            l.setBooking(bookingController.retrieveBookingByBookingId(createNewEval.getBookingId()));
            l.setRemark(createNewEval.getRemark());
            l.setEvaluationTime(createNewEval.getDate());
            l.setScore(createNewEval.getScore());
            System.out.println(createNewEval.getUserId());
            l.setUserEvaluating(userController.retrieveUserById(createNewEval.getUserId()));
            
            Evaluation e = evaluationController.create(l);
            e.getBooking().setEvaluationByGuest(null);
                
            e.getBooking().setEvaluationByHost(null);

            e.getUserEvaluating().getEvaluationsForUserAsGuest().clear();

            e.getUserEvaluating().getEvaluationsForUserAsHost().clear();
            return Response.status(Response.Status.OK).entity(e).build();
        } catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("deleteEval/{id}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEvaluation(@PathParam("id")Long id) {
        try{
            evaluationController.delete(id);
            return Response.status(Response.Status.OK).entity(new ErrorRsp("Success")).build();
        } catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    private EvaluationControllerLocal lookupEvaluationControllerLocal() {
        try {
            Context c = new InitialContext();
            return (EvaluationControllerLocal) c.lookup("java:global/ExperienceSystem/ExperienceSystem-ejb/EvaluationController!stateless.EvaluationControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private BookingControllerLocal lookupBookingControllerLocal() {
        try {
            Context c = new InitialContext();
            return (BookingControllerLocal) c.lookup("java:global/ExperienceSystem/ExperienceSystem-ejb/BookingController!stateless.BookingControllerLocal");
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
