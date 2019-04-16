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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import stateless.BookingControllerLocal;
import stateless.ExperienceControllerLocal;
import stateless.ExperienceDateControllerLocal;
import stateless.UserControllerLocal;
import util.exception.CreateNewBookingException;
import util.exception.ExperienceDateNotFoundException;
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
            b.getExperienceDate().getBookings().clear();
            if (b.getEvaluationByGuest() != null) {
                b.getEvaluationByGuest().setUserEvaluating(null);
            }
            if (b.getEvaluationByHost() != null) {
                b.getEvaluationByHost().setUserEvaluating(null);
            }
            if (b.getCancellationReport() != null) {
                    b.getCancellationReport().setBooking(null);
                }
            b.getGuest().setBookings(null);
        }
        System.out.println("In booking loop of getAllBY Guest");
        return Response.status(Response.Status.OK).entity(new RetrieveAllBookings(ls)).build();
    }

    @Path("getAllBookingsByExperienceId/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBookings(@PathParam("id") Long id) {
        try {
            experienceController.retrieveExperienceById(id);

            List<Booking> ls = bookingController.retrieveAllBookingsByExperienceId(id);

            for (Booking b : ls) {
                if (b.getEvaluationByGuest() != null) {
                    b.getEvaluationByGuest().setBooking(null);
                }
                if (b.getEvaluationByHost() != null) {
                    b.getEvaluationByHost().setBooking(null);
                }
                if (b.getCancellationReport() != null) {
                    b.getCancellationReport().setBooking(null);
                }
                b.getExperienceDate().getBookings().clear();
                b.getGuest().getBookings().clear();
            }

            RetrieveAllBookings r = new RetrieveAllBookings(ls);
            return Response.status(Response.Status.OK).entity(r).build();
        } catch (ExperienceNotFoundException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("getBookingById/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookingById(@PathParam("id") Long id) {
        try {
            Booking b = bookingController.retrieveBookingByBookingId(id);
            if (b == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("No booking").build();
            }
            if (b.getEvaluationByGuest() != null) {
                b.getEvaluationByGuest().setBooking(null);
            }
            if (b.getEvaluationByHost() != null) {
                b.getEvaluationByHost().setBooking(null);
            }
            if (b.getCancellationReport() != null) {
                b.getCancellationReport().setBooking(null);
            }
            b.getExperienceDate().getBookings().clear();
            b.getGuest().getBookings().clear();
            return Response.status(Response.Status.OK).entity(b).build();
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("retrieveAllBookingsByExperienceDateId/{expDateId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveBookingsByExpDate(@PathParam("expDateId") Long expDateId){
        try {
            experienceDateController.retrieveExperienceDateByExperienceDateId(expDateId);
            
            List<Booking> ls = bookingController.retrieveAllBookingsByExperienceDateId(expDateId);

            for (Booking b : ls) {
                if (b.getEvaluationByGuest() != null) {
                    b.getEvaluationByGuest().setBooking(null);
                }
                if (b.getEvaluationByHost() != null) {
                    b.getEvaluationByHost().setBooking(null);
                }
                if (b.getCancellationReport() != null) {
                    b.getCancellationReport().setBooking(null);
                }
                b.getExperienceDate().getBookings().clear();
                b.getGuest().getBookings().clear();
            }

            RetrieveAllBookings r = new RetrieveAllBookings(ls);
            return Response.status(Response.Status.OK).entity(r).build();
        } catch (ExperienceDateNotFoundException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    @Path("createBooking")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooking(CreateNewBooking createNewBooking) throws CreateNewBookingException, InputDataValidationException {
        System.out.println("In BookingResource: createNewBooking");
        try {
            ExperienceDate ed = experienceDateController.retrieveExperienceDateByExperienceDateId(createNewBooking.getExperienceDateId());
            User u = userController.retrieveUserById(createNewBooking.getGuestId());
            Booking newBooking = createNewBooking.getBooking();
            newBooking.setExperienceDate(ed);
            newBooking.setGuest(userController.retrieveUserById(createNewBooking.getGuestId()));
            newBooking = bookingController.createNewBooking(newBooking);
            u.getBookings().add(newBooking);
            userController.updateAccount(u);
            if (newBooking.getEvaluationByGuest() != null) {
                newBooking.getEvaluationByGuest().setBooking(null);
            }
            if (newBooking.getEvaluationByHost() != null) {
                newBooking.getEvaluationByHost().setBooking(null);
            }
            if (newBooking.getCancellationReport() != null) {
                newBooking.getCancellationReport().setBooking(null);
            }
            newBooking.getExperienceDate().getBookings().clear();
            newBooking.getGuest().getBookings().clear();
            return Response.status(Response.Status.OK).entity(newBooking).build();
        } catch (UserNotFoundException | CreateNewBookingException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

//  TODO: Add all fields to the class
    @Path("updateBooking")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBooking(CreateNewBooking createNewBooking) throws InputDataValidationException {
        try {
            ExperienceDate ed = experienceDateController.retrieveExperienceDateByExperienceDateId(createNewBooking.getExperienceDateId());
            Booking b = createNewBooking.getBooking();
//            if (b.getNumberOfPeople() > ed.getSpotsAvailable()) {
//                return Response.status(Response.Status.BAD_REQUEST).entity("Not enough places available. Place available are only: "+ed.getSpotsAvailable()).build();
//            }
            b.setExperienceDate(ed);
            b.setGuest(userController.retrieveUserById(createNewBooking.getGuestId()));
            bookingController.update(b);
            return Response.status(Response.Status.OK).entity(b).build();
        } catch (UserNotFoundException | InputDataValidationException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        } catch (Exception ex) {
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
