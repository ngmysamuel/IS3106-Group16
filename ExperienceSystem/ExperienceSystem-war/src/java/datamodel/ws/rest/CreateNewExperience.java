/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.Experience;

/**
 *
 * @author samue
 */

public class CreateNewExperience {
    private String username;
    private String password;
    private Experience experienceEntity;
    private Long categoryId, locationId, languageId, typeId;

    public CreateNewExperience(String username, String password, Experience experienceEntity, Long categoryId, Long locationId, Long languageId, Long typeId) {
        this.username = username;
        this.password = password;
        this.experienceEntity = experienceEntity;
        this.categoryId = categoryId;
        this.locationId = locationId;
        this.languageId = languageId;
        this.typeId = typeId;
    }

    public CreateNewExperience() {
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

    public Experience getExperienceEntity() {
        return experienceEntity;
    }

    public void setExperienceEntity(Experience experienceEntity) {
        this.experienceEntity = experienceEntity;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
    
    
}
