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
import util.exception.ExperienceNotActiveException;
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
    
    public void update(User user) throws InputDataValidationException, UpdateUserException;
    
    public User login(String username, String password) throws InvalidLoginCredentialException;
    
    public void followExperience(Long id, User user);
    public void unfollowExperience(Long id, User user);
    public User retrieveUserByUsername(String username) throws UserNotFoundException;
    public User retrieveUserById(Long id) throws UserNotFoundException;
    public List<Experience> retrieveAllExperience(Long id);
    public List<Experience> retrieveAllUpcomingExperienceDates(Long id);
    public List<ExperienceDate> retrieveAllHostExperience(Long id);
    public List<ExperienceDate> retrieveAllUpcomingHostExperienceDates(Long id);
    public void deleteHostExperienceDate(Long expId, Long id, String r) throws InvalidLoginCredentialException;

    List<User> retrieveAllFollowingUsers(Long guestId) throws UserNotFoundException;

    boolean isFollowingUser(Long userId, Long followingId) throws UserNotFoundException;

    User unfollowUser(Long userId, Long unfollowId) throws UserNotFoundException;

    Appeal createAppeal(Long userId, Appeal appeal) throws UserNotFoundException;

    User updatePersonalInformation(User user);

    public List<User> retrieveAllUsers();
}
