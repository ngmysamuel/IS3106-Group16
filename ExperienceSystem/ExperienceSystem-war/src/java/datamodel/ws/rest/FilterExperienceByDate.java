/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.Experience;
import java.util.Date;
import java.util.List;

/**
 *
 * @author samue
 */
public class FilterExperienceByDate {
    private List<Experience> listToFilterFrom;
    private Date date;

    public FilterExperienceByDate(List<Experience> listToFilterFrom, Date date) {
        this.listToFilterFrom = listToFilterFrom;
        this.date = date;
    }

    public FilterExperienceByDate() {
    }

    public List<Experience> getListToFilterFrom() {
        return listToFilterFrom;
    }

    public void setListToFilterFrom(List<Experience> listToFilterFrom) {
        this.listToFilterFrom = listToFilterFrom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
