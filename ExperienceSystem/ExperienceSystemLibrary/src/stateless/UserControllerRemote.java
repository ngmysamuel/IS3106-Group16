/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.User;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.RegisterUserException;

/**
 *
 * @author samue
 */
public interface UserControllerRemote {
    public User register(User user) throws InputDataValidationException, RegisterUserException;
}
