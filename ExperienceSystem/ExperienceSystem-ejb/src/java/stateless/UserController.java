/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import com.sun.mail.imap.YoungerTerm;
import entity.Booking;
import entity.Category;
import entity.Experience;
import entity.ExperienceDate;
import entity.Language;
import entity.Location;
import entity.User;
import entity.Type;
import enumerated.StatusEnum;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;

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
        } catch (StaffNotFoundException ex) {
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

    public User retrieveUserByUsername(String username) throws StaffNotFoundException {
        Query q = em.createQuery("SELECT u FROM User WHERE u.username = :name");
        q.setParameter("name", username);
        User user = new User();
        try {
            user = (User) q.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new StaffNotFoundException("No such staff");
        }
        return user;
    }

    public User retrieveUserById(Long id) throws StaffNotFoundException {
        User user = em.find(User.class, id);
        if (user != null) {
            return user;
        } else {
            throw new StaffNotFoundException("Staff ID " + id + " does not exist!");
        }
    }
    
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
    
    public void createHostExperience(Experience exp, Long id) {
        User user = em.find(User.class, id);
        exp = experienceController.createNewExperience(exp);
        user.getExperienceHosted().add(exp);
    }
    
    public void deleteHostExperience(Long expId, Long id, String r) throws InvalidLoginCredentialException {
        Experience exp = em.find(Experience.class, expId);
        User user = em.find(User.class, id);
        if (user.getUserId() != exp.getHost().getUserId()) {
            throw new InvalidLoginCredentialException("You are not the host");
        }
        experienceController.deleteExperience(expId, r);
    }
    
    public void deleteHostExperienceDate(Long expId, Long id, String r) throws InvalidLoginCredentialException {
        ExperienceDate expDate = em.find(ExperienceDate.class, expId);
        User user = em.find(User.class, id);
        if (user.getUserId() != expDate.getExperience().getHost().getUserId()) {
            throw new InvalidLoginCredentialException("You are not the host");
        }
        experienceDateController.deleteExperienceDate(expId, r);
    }
    
    
    

}
