/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.CreateNewBooking;
import datamodel.ws.rest.RetrieveAllBookings;
import entity.Booking;
import enumerated.StatusEnum;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import stateless.BookingControllerLocal;
import stateless.UserControllerLocal;
import util.exception.CreateNewBookingException;
import util.exception.InputDataValidationException;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Path("Booking")
public class BookingResource {

    UserControllerLocal userController = lookupUserControllerLocal();

    BookingControllerLocal bookingController = lookupBookingControllerLocal();
    
    @Path("getAllBookingsByExperienceId/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBookings(@PathParam("id")Long id) {
        List<Booking> ls = bookingController.retrieveAllBookingsByExperienceId(id);
        RetrieveAllBookings r = new RetrieveAllBookings(ls);
        return Response.status(Response.Status.OK).entity(r).build();
    }
    
    @Path("getBookingById/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLanuageById(@PathParam("id")Long id) {
        Booking l = bookingController.retrieveBookingByBookingId(id);
        if (l == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No language").build();
        }
        return Response.status(Response.Status.OK).entity(l).build();
    }
    
    @Path("createBooking")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooking(CreateNewBooking createNewBooking) throws CreateNewBookingException, InputDataValidationException{
        Booking l = new Booking();
        l.setBookingDate(createNewBooking.getDate());
        try {
            l.setGuest(userController.retrieveUserById(createNewBooking.getGuestId()));
        } catch (UserNotFoundException ex) {
            Logger.getLogger(BookingResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.NOT_FOUND).entity("No language").build();
        }
        l.setNumberOfPeople(createNewBooking.getNumOfPeople());
        l.setTotalPrice(createNewBooking.getPrice());
        l.setStatus(StatusEnum.valueOf(createNewBooking.getStatus()));
//        try {
            bookingController.createNewBooking(l);
            return Response.status(Response.Status.OK).entity(new CreateNewBooking()).build();
//        } catch (InputDataValidationException ex) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Input data validation failed").build();
//        } catch (CreateNewBookingException ex) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Some excpetion").build();
//        }
    }
    
//  TODO: Add all fields to the class
    @Path("updateBooking/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBooking(@PathParam("id")Long id, CreateNewBooking createNewBooking) throws  InputDataValidationException{
        Booking b = bookingController.retrieveBookingByBookingId(id);
        try {
            b.setGuest(userController.retrieveUserById(createNewBooking.getGuestId()));
        } catch (UserNotFoundException ex) {
            Logger.getLogger(BookingResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.NOT_FOUND).entity("No language").build();
        }
        b.setNumberOfPeople(createNewBooking.getNumOfPeople());
        b.setStatus(StatusEnum.valueOf(createNewBooking.getStatus()));
        b.setTotalPrice(createNewBooking.getPrice());
        b.setBookingDate(createNewBooking.getDate());
        bookingController.update(b);
        return Response.status(Response.Status.OK).entity("Success").build();
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
