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
 * @author Asus
 */
public class RetrieveAllExperienceDatesRsp {
    
    private List<ExperienceDate> experienceDateEntities;

    public RetrieveAllExperienceDatesRsp(List<ExperienceDate> experienceDateEntities) {
        this.experienceDateEntities = experienceDateEntities;
    }

    public RetrieveAllExperienceDatesRsp() {
    }

    public List<ExperienceDate> getExperienceDateEntities() {
        return experienceDateEntities;
    }

    public void setExperienceDateEntities(List<ExperienceDate> experienceDateEntities) {
        this.experienceDateEntities = experienceDateEntities;
    }
    
    
}
