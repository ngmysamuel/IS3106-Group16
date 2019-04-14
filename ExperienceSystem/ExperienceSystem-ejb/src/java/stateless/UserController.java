/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Appeal;
import entity.Booking;
import entity.Experience;
import entity.ExperienceDate;
import entity.User;
import enumerated.StatusEnum;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RegisterUserException;
import util.exception.UpdateUserException;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Stateless
@Local(UserControllerLocal.class)
public class UserController implements UserControllerLocal {

    @EJB
    private ExperienceDateControllerLocal experienceDateController;
    @EJB
    private ExperienceControllerLocal experienceController;
    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public UserController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }


    @Override
    public User register(User user) throws InputDataValidationException, RegisterUserException {
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        
        if(constraintViolations.isEmpty()) {
            try {
                em.persist(user);
                em.flush();
                return user;
            } catch(PersistenceException ex) {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new RegisterUserException("User with the same username already exist");
                }
                else {
                    throw new RegisterUserException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch(Exception ex) {
                throw new RegisterUserException("An unexpected error has occurred: " + ex.getMessage());
            }  
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
        
    }
    
    // TODO:
    // 1. Implement encryption of the user password
    // 2. Lazy fetching of all array attributes
    public User login(String username, String password) throws InvalidLoginCredentialException {
        try {
            User user = retrieveUserByUsername(username);
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (UserNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public void updateAccount(User user) throws InputDataValidationException, UpdateUserException {
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if (constraintViolations.isEmpty()) {
            try {
                User userToUpdate = retrieveUserById(user.getUserId());
                userToUpdate.setUsername(user.getUsername());
                userToUpdate.setFirstName(user.getFirstName());
                userToUpdate.setLastName(user.getLastName());
                userToUpdate.setGender(user.getGender());
                userToUpdate.setPhoneNumber(user.getPhoneNumber());
                userToUpdate.setBirthday(user.getBirthday());
                userToUpdate.setSelfIntro(user.getSelfIntro());
                userToUpdate.setPremium(user.getPremium());
            } catch (UserNotFoundException ex) {
                throw new UpdateUserException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }

    }


    @Override
    public void followUser(Long userIdFollowing, Long userIdFollowed) throws UserNotFoundException {
        User follower = em.find(User.class, userIdFollowing);
        User followee = em.find(User.class, userIdFollowed);
        
        if(follower == null) {
            throw new UserNotFoundException("Follower info is not provided!");
        } else if(followee == null) {
            throw new UserNotFoundException("Followee info is not provided!");
        } else {
            follower.getFollows().add(followee);
            followee.getFollowers().add(follower);
        }
        
    }

    @Override
    public void unfollowUser(Long userIdFollowing, Long userIdFollowed) throws UserNotFoundException {
        User follower = em.find(User.class, userIdFollowing);
        User followee = em.find(User.class, userIdFollowed);
        
        if(follower == null) {
            throw new UserNotFoundException("Follower info is not provided!");
        } else if(followee == null) {
            throw new UserNotFoundException("Followee info is not provided!");
        } else {
            follower.getFollows().remove(followee);
            followee.getFollowers().remove(follower);
        }
    }
    
    @Override
    public void blockUser(Long userIdBlocking, Long userIdBlocked) throws UserNotFoundException {
        User blocker = em.find(User.class, userIdBlocking);
        User blockee = em.find(User.class, userIdBlocked);
        
        if(blocker == null) {
            throw new UserNotFoundException("Blocker info is not provided!");
        } else if(blockee == null) {
            throw new UserNotFoundException("Blockee info is not provided!");
        } else {
            blocker.getBlocks().add(blockee);
            blockee.getBlockers().add(blocker);
        }
        
    }

    @Override
    public void unblockUser(Long userIdBlocking, Long userIdBlocked) throws UserNotFoundException {
        User blocker = em.find(User.class, userIdBlocking);
        User blockee = em.find(User.class, userIdBlocked);
        
        if(blocker == null) {
            throw new UserNotFoundException("Blocker info is not provided!");
        } else if(blockee == null) {
            throw new UserNotFoundException("Blockee info is not provided!");
        } else {
            blocker.getBlocks().remove(blockee);
            blockee.getBlockers().remove(blocker);
        }
    }
    
    
    // Retrieval section
    
    @Override
    public List<User> retrieveAllUsers() {
        Query query = em.createQuery("SELECT c FROM User c ORDER BY c.userId ASC");
        return query.getResultList();
    }

    @Override
    public User retrieveUserByUsername(String username) throws UserNotFoundException {
        Query q = em.createQuery("SELECT u FROM User u WHERE u.username = :name");
        q.setParameter("name", username);
        User user = new User();
        try {
            user = (User) q.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new UserNotFoundException("No such user");
        }
        // lazy fetching
        for (Experience e : user.getExperienceHosted()) {
        }
        return user;
    }

    @Override
    public User retrieveUserById(Long id) throws UserNotFoundException {
        User user = em.find(User.class, id);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("User ID " + id + " does not exist!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<User>> constraintViolations) {
        String msg = "Input data validation error:";
        
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        System.out.println("******** UserController: prepareInputDataValidationErrorsMessage");
        System.out.println("    **** " + msg);
        return msg;
    }
}

