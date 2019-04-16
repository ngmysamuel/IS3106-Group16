/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.User;
import java.util.List;

/**
 *
 * @author Asus
 */
public class RetrieveAllUsersRsp {
    
    private List<User> users;

    public RetrieveAllUsersRsp(List<User> users) {
        this.users = users;
    }

    public RetrieveAllUsersRsp() {
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    
    
}
