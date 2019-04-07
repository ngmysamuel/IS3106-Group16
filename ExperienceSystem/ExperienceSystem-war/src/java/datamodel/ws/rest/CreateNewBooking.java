/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.User;
import enumerated.StatusEnum;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author samue
 */
public class CreateNewBooking {
    private Long guestId;
    private Integer numOfPeople;
    private BigDecimal price;
    private Date date;
    private String status;

    public CreateNewBooking() {
    }

    public CreateNewBooking(Long guestId, Integer numOfPeople, BigDecimal price, Date date, String status) {
        this.guestId = guestId;
        this.numOfPeople = numOfPeople;
        this.price = price;
        this.date = date;
        this.status = status;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public Integer getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(Integer numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    

   

    
    
}
