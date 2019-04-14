/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.Booking;
import entity.User;
import enumerated.StatusEnum;
import java.math.BigDecimal;
import java.util.Date;
import static javax.ws.rs.core.Response.status;

/**
 *
 * @author samue
 */
public class CreateNewBooking {
    private Long guestId, experienceDateId;
    private Booking booking;

    public CreateNewBooking() {
    }

    public CreateNewBooking(Long guestId, Long experienceDateId, Booking booking) {
        this.guestId = guestId;
        this.experienceDateId = experienceDateId;
        this.booking = booking;
    }


    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Long getExperienceDateId() {
        return experienceDateId;
    }

    public void setExperienceDateId(Long experienceDateId) {
        this.experienceDateId = experienceDateId;
    }
    
}
