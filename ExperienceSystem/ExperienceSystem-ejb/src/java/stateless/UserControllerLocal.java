/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.User;
import javax.ejb.Local;

/**
 *
 * @author samue
 */
@Local
public interface UserControllerLocal {
    public User register(User user);
}
