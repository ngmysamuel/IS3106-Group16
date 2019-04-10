/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.User;

/**
 *
 * @author samue
 */
public class RegisterNewUser {
    private User userEntity;

    public RegisterNewUser() {
    }

    public RegisterNewUser(User userEntity) {
        this.userEntity = userEntity;
    }

    public User getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }
}
