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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewExperienceException;
import util.exception.ExperienceDateNotActiveException;
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

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(user);
                em.flush();
                return user;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new RegisterUserException("User with the same username already exist");
                } else {
                    throw new RegisterUserException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch (Exception ex) {
                throw new RegisterUserException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }

    }

    @Override
    public List<User> retrieveAllUsers() {
        Query query = em.createQuery("SELECT c FROM User c ORDER BY c.userId ASC");
        return query.getResultList();
    }

    public User retrieveUserByUsername(String username) throws UserNotFoundException {
        System.out.println("UserController: retrieveUserByUsername-> username: " + username);
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

    public User retrieveUserById(Long id) throws UserNotFoundException {
        User user = em.find(User.class, id);
        if (user != null) {
            user.getEmail();
            return user;
        } else {
            throw new UserNotFoundException("Staff ID " + id + " does not exist!");
        }
    }

    public void update(User user) throws InputDataValidationException, UpdateUserException {
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
            } catch (UserNotFoundException ex) {
                throw new UpdateUserException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }

    }

    //TODO: Add all fields
    public User updateForRest(Long idToUpdate, String username, String password, String email, String firstName, String lastName, Boolean premium) throws InputDataValidationException, UpdateUserException {
        User userToUpdate = new User();
        try {
            userToUpdate = retrieveUserById(idToUpdate);
        } catch (UserNotFoundException ex) {
            throw new UpdateUserException("User not found");
        }
        userToUpdate.setUsername(username);
        userToUpdate.setPassword(password);
        userToUpdate.setFirstName(firstName);
        userToUpdate.setLastName(lastName);
        userToUpdate.setEmail(email);
        userToUpdate.setPremium(premium);
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(userToUpdate);
        if (constraintViolations.isEmpty()) {
            em.merge(userToUpdate);
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
        return userToUpdate;
    }

    // TODO:
    // 1. Implement encryption of the user password
    // 2. Lazy fetching of all array attributes
    public User login(String username, String password) throws InvalidLoginCredentialException {
        try {
            System.out.println("In userController: Login");
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
    public List<Experience> retrieveAllExperience(Long id) { //completed exp
        User user = em.find(User.class, id);
        LocalDate currentDate1 = LocalDate.now();
        Date currentDate = Date.from(currentDate1.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Experience> lsExperiences = new ArrayList<>();
        List<Booking> bookings = user.getBookings();
        for (Booking b : bookings) {
            if (b.getStatus().equals(StatusEnum.ACTIVE) && b.getExperienceDate().getStartDate().compareTo(currentDate) <= 0) {
                lsExperiences.add(b.getExperienceDate().getExperience());
            }
        }
        return lsExperiences;
    }

    @Override
    public List<Experience> retrieveAllUpcomingExperienceDates(Long id) { //not yet comepltered
        User user = em.find(User.class, id);
        LocalDate currentDate1 = LocalDate.now();
        Date currentDate = Date.from(currentDate1.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Experience> lsExperiences = new ArrayList<>();
        List<Booking> bookings = user.getBookings();
        for (Booking b : bookings) {
            if (b.getStatus().equals(StatusEnum.ACTIVE) && b.getExperienceDate().getStartDate().compareTo(currentDate) > 0) {
                lsExperiences.add(b.getExperienceDate().getExperience());
            }
        }
        return lsExperiences;
    }

    @Override
    public List<ExperienceDate> retrieveAllHostExperience(Long id) { //completed
        User user = em.find(User.class, id);
        LocalDate currentDate1 = LocalDate.now();
        Date currentDate = Date.from(currentDate1.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<ExperienceDate> lsExperiences = new ArrayList<>();
        List<Experience> exp = user.getExperienceHosted();
        for (Experience e : exp) {
            if (e.isActive()) {
                List<ExperienceDate> lsExpDates = e.getExperienceDates();
                for (ExperienceDate expDate : lsExpDates) {
                    if (expDate.getStartDate().compareTo(currentDate) <= 0) {
                        lsExperiences.add(expDate);
                    }
                }
            }
        }
        return lsExperiences;
    }

    @Override
    public List<ExperienceDate> retrieveAllUpcomingHostExperienceDates(Long id) { //not yet compelted host
        User user = em.find(User.class, id);
        LocalDate currentDate1 = LocalDate.now();
        Date currentDate = Date.from(currentDate1.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<ExperienceDate> lsExperiences = new ArrayList<>();
        List<Experience> exp = user.getExperienceHosted();
        for (Experience e : exp) {
            if (e.isActive()) {
                List<ExperienceDate> lsExpDates = e.getExperienceDates();
                for (ExperienceDate expDate : lsExpDates) {
                    if (expDate.getStartDate().compareTo(currentDate) > 0) {
                        lsExperiences.add(expDate);
                    }
                }
            }
        }
        return lsExperiences;
    }

    @Override
    public void deleteHostExperienceDate(Long expId, Long id, String r) throws InvalidLoginCredentialException {
        ExperienceDate expDate = em.find(ExperienceDate.class, expId);
        User user = em.find(User.class, id);
        if (user.getUserId() != expDate.getExperience().getHost().getUserId()) {
            throw new InvalidLoginCredentialException("You are not the host");
        }
        try {
            experienceDateController.deleteExperienceDate(expId, r);
        } catch (ExperienceDateNotActiveException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<User> retrieveAllFollowingUsers(Long userId) throws UserNotFoundException {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        List<User> following = user.getFollows();
        for (User u : following) {
            u.getUserId();
        }
        return following;
    }
    
    @Override
    public List<User> retrieveAllFollowers(Long userId) throws UserNotFoundException {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        List<User> followers = user.getFollowers();
        for (User u : followers) {
            u.getUserId();
        }
        return followers;
    }

    @Override
    public boolean isFollowingUser(Long userId, Long followingId) throws UserNotFoundException {
        List<User> following = retrieveAllFollowingUsers(userId);
        for (User u : following) {
            if (u.getUserId() == followingId) {
                return true;
            }
        }

        return false;
    }

    @Override
    public User unfollowUser(Long userId, Long unfollowId) throws UserNotFoundException {
        User unfollow = this.retrieveUserById(userId);
        User user = this.retrieveUserById(unfollowId);
        if (isFollowingUser(userId, unfollowId)) {
            user.getFollows().remove(unfollow);
            unfollow.getFollowers().remove(user);
        }
        return unfollow;
    }
    
    public User followUser(Long userId, Long followId) throws UserNotFoundException{
        User user = this.retrieveUserById(userId);
        User follow = this.retrieveUserById(followId);
        if(!isFollowingUser(userId, followId)){
            user.getFollows().add(follow);
            follow.getFollowers().add(user);
        }
        
        return follow;
    }

    @Override
    public Appeal createAppeal(Long userId, Appeal appeal) throws UserNotFoundException {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        appeal.setUser(user);
        user.getAppeals().add(appeal);
        em.persist(appeal);
        return appeal;
    }

    @Override
    public User updatePersonalInformation(User user) {
        em.merge(user);
        return user;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<User>> constraintViolations) {
        String msg = "Input data validation error:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        System.out.println("******** UserController: prepareInputDataValidationErrorsMessage");
        System.out.println("    **** " + msg);
        return msg;
    }
}
