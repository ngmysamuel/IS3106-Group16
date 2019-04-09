/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.Booking;
import entity.User;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author samue
 */
public class CreateNewEval {
    private Long userId, bookingId;
    private Date date;
    private BigDecimal score;
    private String remark;

    public CreateNewEval(Long userId, Long bookingId, Date date, BigDecimal score, String remark) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.date = date;
        this.score = score;
        this.remark = remark;
    }

    public CreateNewEval() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    
    
}
