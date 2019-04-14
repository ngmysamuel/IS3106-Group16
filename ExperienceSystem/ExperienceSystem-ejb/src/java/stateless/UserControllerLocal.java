/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Appeal;
import entity.Experience;
import entity.ExperienceDate;
import entity.User;
import java.util.List;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RegisterUserException;
import util.exception.UpdateUserException;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */

public interface UserControllerLocal {
    
    public User register(User user) throws InputDataValidationException, RegisterUserException;
    public User login(String username, String password) throws InvalidLoginCredentialException;
    public void updateAccount(User user) throws InputDataValidationException, UpdateUserException;

    // Social network
    public void followUser(Long userIdFollowing, Long userIdFollowed) throws UserNotFoundException;        
    public void unfollowUser(Long userIdFollowing, Long userIdFollowed) throws UserNotFoundException;   
    public void blockUser(Long userIdBlocking, Long userIdBlocked) throws UserNotFoundException;
    public void unblockUser(Long userIdBlocking, Long userIdBlocked) throws UserNotFoundException;
    
    // retrieval
    public List<User> retrieveAllUsers();
    public User retrieveUserByUsername(String username) throws UserNotFoundException;
    public User retrieveUserById(Long id) throws UserNotFoundException;
  
}
