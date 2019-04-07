/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.Experience;

/**
 *
 * @author Asus
 */
public class RetrieveExperienceRsp {
    
    private Experience experienceEntity;

    public RetrieveExperienceRsp(Experience experienceEntity) {
        this.experienceEntity = experienceEntity;
    }

    public RetrieveExperienceRsp() {
    }

    public Experience getExperienceEntity() {
        return experienceEntity;
    }

    public void setExperienceEntity(Experience experienceEntity) {
        this.experienceEntity = experienceEntity;
    }
    
    
    
}
