/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.Booking;
import java.util.List;

/**
 *
 * @author samue
 */
public class RetrieveAllBookings {
    private List<Booking> ls;

    public RetrieveAllBookings() {
    }

    public RetrieveAllBookings(List<Booking> ls) {
        this.ls = ls;
    }

    public List<Booking> getLs() {
        return ls;
    }

    public void setLs(List<Booking> ls) {
        this.ls = ls;
    }
    
    
}
