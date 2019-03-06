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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewExperienceException;
import util.exception.ExperienceNotActiveException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Stateless
public class UserController implements UserControllerRemote, UserControllerLocal {

    @EJB
    private ExperienceDateControllerLocal experienceDateController;

    @EJB
    private ExperienceControllerLocal experienceController;

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public User register(User user) {
        em.persist(user);
        em.flush();
        return user;
    }

    public User login(String username, String password) throws InvalidLoginCredentialException {
        try {
            User user = retrieveUserByUsername(username);
            //String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + staffEntity.getSalt()));
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (UserNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    public void followExperience(Long id, User user) {
        Experience exp = em.find(Experience.class, id);
        experienceController.addFollowerToExperience(id, user);
        user.getFollowedExperiences().add(exp);
    }

    public void unfollowExperience(Long id, User user) {
        Experience exp = em.find(Experience.class, id);
        experienceController.removeFollowerFromExperience(id, user);
        user.getFollowedExperiences().remove(exp);
    }

    public User retrieveUserByUsername(String username) throws UserNotFoundException {
        Query q = em.createQuery("SELECT u FROM User WHERE u.username = :name");
        q.setParameter("name", username);
        User user = new User();
        try {
            user = (User) q.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new UserNotFoundException("No such user");
        }
        return user;
    }

    public User retrieveUserById(Long id) throws UserNotFoundException {
        User user = em.find(User.class, id);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("Staff ID " + id + " does not exist!");
        }
    }
    
    @Override
    public List<Experience> retrieveAllExperience(Long id) { //completed exp
        User user = em.find(User.class, id);
        LocalDate currentDate = LocalDate.now();
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
        LocalDate currentDate = LocalDate.now();
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
        LocalDate currentDate = LocalDate.now();
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
        LocalDate currentDate = LocalDate.now();
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
    public void createHostExperience(Experience exp, Long id) {
        User user = em.find(User.class, id);
        try {
            exp = experienceController.createNewExperience(exp);
        } catch (CreateNewExperienceException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InputDataValidationException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        user.getExperienceHosted().add(exp);
    }
    
   
    @Override
    public void deleteHostExperience(Long expId, Long id, String r) throws InvalidLoginCredentialException, ExperienceNotActiveException {
        Experience exp = em.find(Experience.class, expId);
        User user = em.find(User.class, id);
        if (user.getUserId() != exp.getHost().getUserId()) {
            throw new InvalidLoginCredentialException("You are not the host");
        }
        experienceController.deleteExperience(expId, r);
    }
    
    @Override
    public void deleteHostExperienceDate(Long expId, Long id, String r) throws InvalidLoginCredentialException {
        ExperienceDate expDate = em.find(ExperienceDate.class, expId);
        User user = em.find(User.class, id);
        if (user.getUserId() != expDate.getExperience().getHost().getUserId()) {
            throw new InvalidLoginCredentialException("You are not the host");
        }
        experienceDateController.deleteExperienceDate(expId, r);
    }

    @Override
    public List<User> retrieveAllFollowingUsers(Long userId) throws UserNotFoundException {
        User user = em.find(User.class, userId);
        if(user == null){
            throw new UserNotFoundException();
        }
        List<User> following = user.getFollows();
        for(User u: following){
            u.getUserId();
        }
        return following;
    }

    @Override
    public boolean isFollowingUser(Long userId, Long followingId) throws UserNotFoundException{
        List<User> following = retrieveAllFollowingUsers(userId);
        for(User u: following){
            if(u.getUserId() == followingId){
                return true;
            }
        }
        
        return false;
    }

    @Override
    public User unfollowUser(Long userId, Long unfollowId) throws UserNotFoundException {
        User unfollow = em.find(User.class, unfollowId);
        if(unfollow == null){
            throw new UserNotFoundException();
        }
        if(isFollowingUser(userId, unfollowId)){
            User user = em.find(User.class, userId);
            user.getFollows().remove(unfollow);
            unfollow.getFollowers().remove(user);
        }
        return unfollow;
    }

    @Override
    public Appeal createAppeal(Long userId, Appeal appeal) throws UserNotFoundException {
        User user = em.find(User.class, userId);
        if(user == null){
            throw new UserNotFoundException();
        }
        appeal.setUser(user);
        user.getAppeals().add(appeal);
        em.persist(appeal);
        return appeal;
    }

    @Override
    public User updatePersonalInformation(User user){
        em.merge(user);
        return user;
    }
    
    
    
    

}
