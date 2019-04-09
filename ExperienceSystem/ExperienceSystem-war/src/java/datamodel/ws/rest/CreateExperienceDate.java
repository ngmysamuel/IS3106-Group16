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
public class CreateExperienceDate {
  
    private String username;
    private String password;
    private ExperienceDate experienceDateEntity;
    private Long experienceId;

    public CreateExperienceDate(String username, String password, ExperienceDate experienceDateEntity, Long experienceId) {
        this.username = username;
        this.password = password;
        this.experienceDateEntity = experienceDateEntity;
        this.experienceId = experienceId;
    }

    public CreateExperienceDate() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ExperienceDate getExperienceDateEntity() {
        return experienceDateEntity;
    }

    public void setExperienceDateEntity(ExperienceDate experienceDateEntity) {
        this.experienceDateEntity = experienceDateEntity;
    }

    public Long getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Long experienceId) {
        this.experienceId = experienceId;
    }
    
    
}
