/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.CreateNewBooking;
import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.RetrieveAllBookings;
import entity.Booking;
import entity.ExperienceDate;
import enumerated.StatusEnum;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import stateless.BookingControllerLocal;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;
import stateless.UserControllerLocal;
import util.exception.CreateNewBookingException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Path("Booking")
public class BookingResource {

    ExperienceDateControllerLocal experienceDateController = lookupExperienceDateControllerLocal();

    ExperienceControllerLocal experienceController = lookupExperienceControllerLocal();

    UserControllerLocal userController = lookupUserControllerLocal();

    BookingControllerLocal bookingController = lookupBookingControllerLocal();
    
    @Path("getAllBookingsByGuestId/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBookingsByGuestId(@PathParam("id") Long id) {
        List<Booking> ls = bookingController.retrieveAllBookingsByGuestId(id);
        for (Booking b : ls) {
            b.getExperienceDate().setBookings(null);
            b.getEvaluationByGuest().setUserEvaluating(null);
            b.getEvaluationByHost().setUserEvaluating(null);
        }
        return Response.status(Response.Status.OK).entity(new RetrieveAllBookings(ls)).build();
    }
    
    @Path("getAllBookingsByExperienceId/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBookings(@PathParam("id")Long id) {
        try{
            experienceController.retrieveExperienceById(id);
            
            List<Booking> ls = bookingController.retrieveAllBookingsByExperienceId(id);
            
            for(Booking b: ls){
               if(b.getEvaluationByGuest() != null){
                b.getEvaluationByGuest().setBooking(null);
                }
                if(b.getEvaluationByHost() != null){
                    b.getEvaluationByHost().setBooking(null);
                }
                if(b.getCancellationReport() != null){
                    b.getCancellationReport().setBooking(null);
                }
                b.getExperienceDate().getBookings().clear();
                b.getGuest().getBookings().clear();
            }
            
            RetrieveAllBookings r = new RetrieveAllBookings(ls);
            return Response.status(Response.Status.OK).entity(r).build();
        } catch (ExperienceNotFoundException ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        } catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("getBookingById/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookingById(@PathParam("id")Long id) {
        try{
            Booking b = bookingController.retrieveBookingByBookingId(id);
            if (b == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("No booking").build();
            }
            if(b.getEvaluationByGuest() != null){
                b.getEvaluationByGuest().setBooking(null);
            }
            if(b.getEvaluationByHost() != null){
                b.getEvaluationByHost().setBooking(null);
            }
            if(b.getCancellationReport() != null){
                b.getCancellationReport().setBooking(null);
            }
            b.getExperienceDate().getBookings().clear();
            b.getGuest().getBookings().clear();
            return Response.status(Response.Status.OK).entity(b).build();
        } catch(Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("createBooking")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooking(CreateNewBooking createNewBooking) throws CreateNewBookingException, InputDataValidationException{
System.out.println("In BookingResource: createNewBooking");
        try{
            ExperienceDate ed = experienceDateController.retrieveExperienceDateByDateId(createNewBooking.getExperienceDateId());
            Booking l = new Booking();
            l.setExperienceDate(ed);
            l.setBookingDate(createNewBooking.getDate());
            l.setGuest(userController.retrieveUserById(createNewBooking.getGuestId()));
            l.setNumberOfPeople(createNewBooking.getNumOfPeople());
            l.setTotalPrice(createNewBooking.getPrice());
            l.setStatus(StatusEnum.valueOf(createNewBooking.getStatus()));
System.out.println("About to go into the Booking Controller");
            Booking newBooking = bookingController.createNewBooking(l);
System.out.println("Back from the Booking Controller");
            return Response.status(Response.Status.OK).entity(newBooking).build();
        } catch (UserNotFoundException | CreateNewBookingException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        } catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
//  TODO: Add all fields to the class
    @Path("updateBooking/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBooking(@PathParam("id")Long id, CreateNewBooking createNewBooking) throws  InputDataValidationException{
        try{
            Booking l = bookingController.retrieveBookingByBookingId(id);
            l.setBookingDate(createNewBooking.getDate());
            l.setGuest(userController.retrieveUserById(createNewBooking.getGuestId()));
            l.setNumberOfPeople(createNewBooking.getNumOfPeople());
            l.setTotalPrice(createNewBooking.getPrice());
            l.setStatus(StatusEnum.valueOf(createNewBooking.getStatus()));
            bookingController.update(l);
            return Response.status(Response.Status.OK).entity(l).build();
        } catch (UserNotFoundException | InputDataValidationException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        } catch (Exception ex){
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
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

    private ExperienceControllerLocal lookupExperienceControllerLocal() {
        try {
            Context c = new InitialContext();
            return (ExperienceControllerLocal) c.lookup("java:global/ExperienceSystem/ExperienceSystem-ejb/ExperienceController!stateless.ExperienceControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ExperienceDateControllerLocal lookupExperienceDateControllerLocal() {
        try {
            Context c = new InitialContext();
            return (ExperienceDateControllerLocal) c.lookup("java:global/ExperienceSystem/ExperienceSystem-ejb/ExperienceDateController!stateless.ExperienceDateControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
