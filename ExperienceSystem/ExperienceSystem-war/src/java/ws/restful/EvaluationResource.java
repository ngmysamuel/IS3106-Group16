/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.CreateNewEval;
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
        List<Evaluation> ls = evaluationController.retrieveAllEvaluations();
        RetrieveAllEvaluations r = new RetrieveAllEvaluations(ls);
        return Response.status(Response.Status.OK).entity(r).build();
    }
    
    @Path("getEvalById/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvaluationById(@PathParam("id")Long id) {
        Evaluation l = evaluationController.retrieveEvaluationById(id);
        if (l == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No language").build();
        }
        return Response.status(Response.Status.OK).entity(l).build();
    }
    
    @Path("createEval")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEvaluation(CreateNewEval createNewEval) throws UserNotFoundException {
        Evaluation l = new Evaluation();
        l.setBooking(bookingController.retrieveBookingByBookingId(createNewEval.getBookingId()));
        l.setEvaluationTime(createNewEval.getDate());
        l.setScore(createNewEval.getScore());
        System.out.println(createNewEval.getUserId());
//        try {
            l.setUserEvaluating(userController.retrieveUserById(createNewEval.getUserId()));
//        } catch (UserNotFoundException ex) {
//            System.out.println(ex);
//            return Response.status(Response.Status.NOT_FOUND).entity("No user").build();
//            
//        }
        evaluationController.create(l);
        return Response.status(Response.Status.OK).entity(new CreateNewEval(l.getUserEvaluating().getUserId(), createNewEval.getBookingId(), createNewEval.getDate(), createNewEval.getScore())).build();
    }
    
    @Path("deleteEval/{id}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    public Response deleteEvaluation(@PathParam("id")Long id) {
        evaluationController.delete(id);
        return Response.status(Response.Status.OK).entity("Success").build();
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
