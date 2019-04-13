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
//Can add ALL fields in the furture
public class UpdateUser {
    private User userEntity;

    public UpdateUser() {
    }

    public UpdateUser(User userEntity) {
        this.userEntity = userEntity;
    }

    public User getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

}
