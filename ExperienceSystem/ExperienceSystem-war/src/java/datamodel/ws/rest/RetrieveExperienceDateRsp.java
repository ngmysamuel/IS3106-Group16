/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.ExperienceDate;

/**
 *
 * @author Asus
 */
public class RetrieveExperienceDateRsp {
    
    private ExperienceDate experienceDateEntity;

    public RetrieveExperienceDateRsp(ExperienceDate experienceDateEntity) {
        this.experienceDateEntity = experienceDateEntity;
    }

    public RetrieveExperienceDateRsp() {
    }

    public ExperienceDate getExperienceDateEntity() {
        return experienceDateEntity;
    }

    public void setExperienceDateEntity(ExperienceDate experienceDateEntity) {
        this.experienceDateEntity = experienceDateEntity;
    }
    
    
}
