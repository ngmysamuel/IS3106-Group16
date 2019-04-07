/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.ExperienceDate;
import java.util.List;

/**
 *
 * @author samue
 */
public class RetrieveListOfExperienceDates {
    private List<ExperienceDate> ls;

    public RetrieveListOfExperienceDates() {
    }

    public RetrieveListOfExperienceDates(List<ExperienceDate> ls) {
        this.ls = ls;
    }

    public List<ExperienceDate> getLs() {
        return ls;
    }

    public void setLs(List<ExperienceDate> ls) {
        this.ls = ls;
    }
    
    
}
