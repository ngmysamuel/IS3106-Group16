/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Booking;
import entity.Category;
import entity.Experience;
import entity.ExperienceDate;
import entity.ExperienceDateCancellationReport;
import entity.Language;
import entity.Location;
import entity.Type;
import entity.User;
import enumerated.StatusEnum;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewExperienceException;
import util.exception.DeleteExperienceException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateEperienceInfoException;


@Stateless
public class ExperienceController implements ExperienceControllerLocal {

    @EJB
    private LanguageControllerLocal languageControllerLocal;
    @EJB
    private TypeControllerLocal typeControllerLocal;
    @EJB
    private CategoryControllerLocal categoryControllerLocal;
    @EJB
    private LocationControllerLocal locationControllerLocal;
    @EJB
    private ExperienceDateCancellationReportControllerLocal experienceDateCancellationReportControllerLocal;
    @EJB
    private ExperienceDateControllerLocal experienceDateControllerLocal;

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ExperienceController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    
    // Create, Update, Delete section
    
    // When an experience is successfully created, it will be attached to the host's experience list
    @Override
    public Experience createNewExperience(Experience newExperience) throws CreateNewExperienceException, InputDataValidationException {
        Category category = categoryControllerLocal.retrieveCategoryById(newExperience.getCategory().getCategoryId());
        Type type = typeControllerLocal.retrieveTypeById(newExperience.getType().getTypeId());
        Language language = languageControllerLocal.retrieveLanguageById(newExperience.getLanguage().getLanguageId());
        Location location = locationControllerLocal.retrieveLocationById(newExperience.getLocation().getLocationId());

        if (category == null || type == null || language == null || location == null) {
            throw new InputDataValidationException("Category/Type/Language/Location information is not provided!");
        }

        newExperience.setCategory(category);
        newExperience.setType(type);
        newExperience.setLanguage(language);
        newExperience.setLocation(location);

        newExperience.setActive(true);

        Set<ConstraintViolation<Experience>> constraintViolations = validator.validate(newExperience);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newExperience);
                em.flush();
                User host = newExperience.getHost();
                host.getExperienceHosted().add(newExperience);
                return newExperience;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewExperienceException("Experience with the same title already exists");
                } else {
                    throw new CreateNewExperienceException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch (Exception ex) {
                throw new CreateNewExperienceException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public void updateExperienceInformation(Experience experience) throws UpdateEperienceInfoException {
        try {
            Experience experienceToUpdate = retrieveExperienceById(experience.getExperienceId());
            Category category = categoryControllerLocal.retrieveCategoryById(experience.getCategory().getCategoryId());
            Type type = typeControllerLocal.retrieveTypeById(experience.getType().getTypeId());
            Language language = languageControllerLocal.retrieveLanguageById(experience.getLanguage().getLanguageId());
            Location location = locationControllerLocal.retrieveLocationById(experience.getLocation().getLocationId());

            if (category == null || type == null || language == null || location == null) {
                throw new UpdateEperienceInfoException("Category/Type/Language/Location information is not provided!");
            }

            experienceToUpdate.setCategory(category);
            experienceToUpdate.setType(type);
            experienceToUpdate.setLanguage(language);
            experienceToUpdate.setLocation(location);

            Set<ConstraintViolation<Experience>> constraintViolations = validator.validate(experience);
            if (constraintViolations.isEmpty()) {
                experienceToUpdate.setTitle(experience.getTitle());
                experienceToUpdate.setDescription(experience.getDescription());
                experienceToUpdate.setProvidingItems(experience.getProvidingItems());
                experienceToUpdate.setRequiringItems(experience.getRequiringItems());
            } else {
                throw new UpdateEperienceInfoException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (ExperienceNotFoundException ex) {
            throw new UpdateEperienceInfoException(ex.getMessage());
        }

    }

    // TODO: send notifications to the guest of originally upcoming ExperienceDate + refund guests
    @Override
    public void deleteExperience(Long experienceId) throws DeleteExperienceException {
        try {
            Experience experienceToDelete = retrieveExperienceById(experienceId);
            if (!experienceToDelete.isActive()) {
                throw new DeleteExperienceException("This experience is no longer active!");
            } else {
                experienceToDelete.setActive(false);

                LocalDate current1 = LocalDate.now();
                Date current = Date.from(current1.atStartOfDay(ZoneId.systemDefault()).toInstant());

                // Force cancel the coming experience date listings
                // Create ExperienceDateCancellationReport
                List<ExperienceDate> experienceDates = experienceToDelete.getExperienceDates();
                for (ExperienceDate date : experienceDates) {
                    if (date.getStartDate().compareTo(current) > 0) {
                        List<Booking> bookings = date.getBookings();
                        for (Booking b : bookings) {
                            b.setStatus(StatusEnum.CANCELLED);
                            ExperienceDateCancellationReport rpt = new ExperienceDateCancellationReport();
                            rpt.setBooking(b);
                            rpt.setExperienceDate(date);
                            rpt.setCancellationReason("The host of this experience cancels the whole experience.");
                            try{
                                experienceDateCancellationReportControllerLocal.createNewExperienceDateCancellationReport(rpt);
                            }catch(InputDataValidationException ex) {
                                throw new DeleteExperienceException(ex.getMessage());
                            }
                        }
                    }
                }
            }
        } catch (ExperienceNotFoundException ex) {
            throw new DeleteExperienceException(ex.getMessage());
        }

    }

    
    
    // Retrieving section
    
    @Override
    public Experience retrieveExperienceById(Long experienceId) throws ExperienceNotFoundException {
        System.out.println("******** ExperienceController: retrieveExperienceById");
        Experience e = em.find(Experience.class, experienceId);
        if (e == null) {
            throw new ExperienceNotFoundException();
        }
        System.out.println("**** this experience has " + e.getExperienceDates().size() + " experience dates");
        System.out.println("-----------------------------");
        e.getExperienceDates().size();
        e.getFollowers();
        e.getProvidingItems();
        e.getRequiringItems();
        e.getReminders();
        return e;
    }
    
    @Override
    public List<Experience> retrieveAllExperiences() {
        System.out.println("******** ExperienceController: retrieveAllExperiences()");
        Query query = em.createQuery("SELECT e FROM Experience e ORDER BY e.experienceId ASC");
        List<Experience> exps = query.getResultList();
        System.out.println("    **** experience size: " + exps.size());
        for (Experience e : exps) {
            System.out.println("....");
            System.out.println("**** experience: " + e.getTitle());
            System.out.println("**** experience date size: " + e.getExperienceDates().size());
            e.getExperienceDates().size();
        }
        return exps;
    }

    //Retrieve all host experiences hosted by a given user
    @Override
    public List<Experience> retrieveAllHostExperiencesByHostId(Long hostUserId) {
        Query query = em.createQuery("SELECT e FROM Experience e WHERE e.host.userId = :inHostUserId ORDER BY e.experienceId ASC");
        query.setParameter("inHostUserId", hostUserId);

        List<Experience> hostExperiences = query.getResultList();
        return hostExperiences;
    }

    //Retrieve all active host experiences hosted by a given user
    @Override
    public List<Experience> retrieveAllActiveHostExperiencesByHostId(Long hostUserId) {
        List<Experience> hostExperiences = retrieveAllHostExperiencesByHostId(hostUserId);
        ListIterator listIterator = hostExperiences.listIterator();
        while (listIterator.hasNext()) {
            Experience experience = (Experience) listIterator.next();
            if (!experience.isActive()) {
                listIterator.remove();
            }
        }
        return hostExperiences;
    }

    @Override
    public List<Experience> retrieveFavouriteExperiences(Long userId){
        Query query = em.createQuery("SELECT e FROM Experience e, IN (e.followers) f WHERE f.userId = :inUserId");
        query.setParameter("inUserId", userId);
        
        List<Experience> favouriteExperiences = query.getResultList();
        return favouriteExperiences; 
    }
    
    @Override
    public List<Experience> retrievePastExperiences(Long userId){
        Query query = em.createQuery("SELECT b FROM Booking b JOIN b.guest k JOIN b.experienceDate e WHERE k.userId = :inUserId AND e.startDate < :today");
        Calendar todaysDateObject = null;
        query.setParameter("today", todaysDateObject,TemporalType.DATE);
        query.setParameter("inUserId", userId);
        List<Booking> bookings = query.getResultList();
        
        List<Experience> experiences = null;  
        if(!bookings.isEmpty()||bookings.size()!=0){
            for(Booking booking: bookings){
                experiences.add(booking.getExperienceDate().getExperience());
            }
        }
        
        return experiences;
    }
    
    @Override
    public List<Experience> retrieveUpcomingExperiences(Long userId){
        Query query = em.createQuery("SELECT b FROM Booking b JOIN b.guest g JOIN b.experienceDate e WHERE g.userId = :inUserId AND e.startDate >= :today");
        Calendar todaysDateObject = null;
        query.setParameter("today", todaysDateObject,TemporalType.DATE);
        query.setParameter("inUserId", userId);
        List<Booking> upcomingBookings = query.getResultList();
        List<Experience> experiences = null;
        
        for(Booking booking: upcomingBookings){
            experiences.add(booking.getExperienceDate().getExperience());
        }
        return experiences;
    }
    
    @Override
    public List<Experience> retrieveTopRatedExperience() {
        Query query = em.createQuery("SELECT DISTINCT e FROM Experience e WHERE e.host.premium = true OR e.averageScore >= 3.5 ORDER BY e.experienceId DESC");
        List<Experience> exps = query.getResultList();
        for (Experience e : exps) {
            e.getExperienceDates();
        }
        return exps;
    }

    
    // Experience followers section
    
    @Override
    public void removeFollowerFromExperience(Long experienceId, Long userId) {
        Experience experience = em.find(Experience.class, experienceId);
        User user = em.find(User.class, userId);

        if (experience != null && user != null) {
            experience.getFollowers().remove(user);
            user.getFollowedExperiences().remove(experience);
        }
    }

    @Override
    public void addFollowerToExperience(Long experienceId, Long userId) {
        Experience experience = em.find(Experience.class, experienceId);
        User user = em.find(User.class, userId);

        if (experience != null && user != null) {
            experience.getFollowers().add(user);
            user.getFollowedExperiences().add(experience);
        }
    }

    @Override
    public List<User> retrieveExperienceFollowers(Long experienceId) {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.followedExperiences.experienceId = :inExperienceId");
        query.setParameter("inExperienceId", experienceId);
        
        List<User> followers = query.getResultList();
        if(followers == null || followers.isEmpty() || followers.get(0) == null){
            return new ArrayList<>();
        }
        for(User u: followers){
            u.getEmail();
        }
        return followers;
    }
      
    // Filtering section
    
    // return a list of experiences which are active
    @Override
    public List<Experience> filterExperienceByActiveState(List<Experience> experienceList) {
        System.out.println("******** ExperienceController: filterExperienceByActiveState()");
        System.out.println("    **** experienceList.size(): " + experienceList.size());
        ListIterator iterator = experienceList.listIterator();

        while (iterator.hasNext()) {
            Experience experience = (Experience) iterator.next();

            if (!experience.isActive()) {
                iterator.remove();
            }
        }

        System.out.println("**** remaining experience size: " + experienceList.size());
        System.out.println("...");
        return experienceList;
    }

    // return a list of experiences which have at least one experience dates whose date matches the filtering date
    @Override
    public List<Experience> filterExperienceByDate(List<Experience> experienceList, Date filteringDate) {
        System.out.println("******** ExperienceController: filterExperienceByDate()");
        System.out.println("    **** experienceList.size(): " + experienceList.size());
        System.out.println("    **** filteringDate: " + filteringDate.toString());
        ListIterator iterator = experienceList.listIterator();

        while (iterator.hasNext()) {
            Experience experience = (Experience) iterator.next();

            // retrieve the list of active experience dates of this experience
            List<ExperienceDate> activeExperienceDates = experienceDateControllerLocal.retrieveAllActiveExperienceDatesByExperienceId(experience.getExperienceId());
            List<ExperienceDate> experienceDatesMatched = new ArrayList();

            // remove the experience if the experience does not have any active experience date whose date matches the filtering date
            for (ExperienceDate experienceDate : activeExperienceDates) {
                if (experienceDate.getStartDate().compareTo(filteringDate) == 0) {
                    experienceDatesMatched.add(experienceDate);
                }
            }
            if (experienceDatesMatched.size() == 0) {
                iterator.remove();
            } else {
                experience.setExperienceDates(experienceDatesMatched);
            }
        }

        System.out.println("**** remaining experience size: " + experienceList.size());
        System.out.println("...");
        return experienceList;
    }

    // return a list of experiences which have at least one experience dates which has the number of slots available
    @Override
    public List<Experience> filterExperienceBySlotsAvailable(List<Experience> experienceList, Integer numOfPeople) {
        System.out.println("******** ExperienceController: filterExperienceBySlotsAvailable()");
        System.out.println("    **** experienceList.size(): " + experienceList.size());
        System.out.println("    **** numOfPeople: " + numOfPeople);
        ListIterator iterator = experienceList.listIterator();
        System.out.println("....");
        while (iterator.hasNext()) {
            Experience experience = (Experience) iterator.next();
            System.out.println("**** experience: " + experience.getTitle());

            List<ExperienceDate> availableExperienceDates = experience.getExperienceDates();
            ListIterator iterator2 = availableExperienceDates.listIterator();
            while (iterator2.hasNext()) {
                ExperienceDate experienceDate = (ExperienceDate) iterator2.next();
                System.out.println("**** experienceDate: " + experienceDate.getExperienceDateId());
                System.out.println("**** spots available: " + experienceDate.getSpotsAvailable());
                if (experienceDate.getSpotsAvailable() < numOfPeople) {
                    iterator2.remove();
                }
            }

            // remove the experience if it does not have any active experience date with slots available for the request
            if (availableExperienceDates.isEmpty()) {
                iterator.remove();
            }
        }

        System.out.println("**** remaining experience size: " + experienceList.size());
        System.out.println("...");
        return experienceList;
    }

    // return a list of experiences which have the required category
    @Override
    public List<Experience> filterExperienceByCategory(List<Experience> experienceList, Long categoryId) {
        System.out.println("******** ExperienceController: filterExperienceByCategory()");
        System.out.println("    **** experienceList.size(): " + experienceList.size());
        System.out.println("    **** categoryId: " + categoryId);
        if (categoryId != null && categoryId.compareTo(new Long(0)) != 0) {
            ListIterator iterator = experienceList.listIterator();

            while (iterator.hasNext()) {
                Experience experience = (Experience) iterator.next();

                if (experience.getCategory().getCategoryId().compareTo(categoryId) != 0) {
                    iterator.remove();
                }
            }
        }

        System.out.println("**** remaining experience size: " + experienceList.size());
        System.out.println("...");
        return experienceList;
    }

    // return a list of experiences which have the required type
    @Override
    public List<Experience> filterExperienceByType(List<Experience> experienceList, Long typeId) {
        System.out.println("******** ExperienceController: filterExperienceByType()");
        System.out.println("    **** experienceList.size(): " + experienceList.size());
        System.out.println("    **** typeId: " + typeId);
        if (typeId != null && typeId.compareTo(new Long(0)) != 0) {
            ListIterator iterator = experienceList.listIterator();

            while (iterator.hasNext()) {
                Experience experience = (Experience) iterator.next();

                if (experience.getType().getTypeId().compareTo(typeId) != 0) {
                    iterator.remove();
                }
            }
        }

        System.out.println("**** remaining experience size: " + experienceList.size());
        System.out.println("...");
        return experienceList;
    }

    // return a list of experiences which have the required language
    @Override
    public List<Experience> filterExperienceByLanguage(List<Experience> experienceList, Long languageId) {
        System.out.println("******** ExperienceController: filterExperienceByLanguage()");
        System.out.println("    **** experienceList.size(): " + experienceList.size());
        System.out.println("    **** languageId: " + languageId);
        if (languageId != null && languageId.compareTo(new Long(0)) != 0) {
            ListIterator iterator = experienceList.listIterator();

            while (iterator.hasNext()) {
                Experience experience = (Experience) iterator.next();

                if (experience.getLanguage().getLanguageId().compareTo(languageId) != 0) {
                    iterator.remove();
                }
            }
        }

        System.out.println("**** remaining experience size: " + experienceList.size());
        System.out.println("...");
        return experienceList;
    }

    // return a list of experiences which have the required type
    @Override
    public List<Experience> filterExperienceByLocation(List<Experience> experienceList, Long locationId) {
        System.out.println("******** ExperienceController: filterExperienceByLocation()");
        System.out.println("    **** experienceList.size(): " + experienceList.size());
        System.out.println("    **** locationId: " + locationId);

        if (locationId != null && locationId.compareTo(new Long(0)) != 0) {
            ListIterator iterator = experienceList.listIterator();

            while (iterator.hasNext()) {
                Experience experience = (Experience) iterator.next();

                if (experience.getLocation().getLocationId().compareTo(locationId) != 0) {
                    iterator.remove();
                }
            }
        }

        System.out.println("**** remaining experience size: " + experienceList.size());
        System.out.println("...");
        return experienceList;

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Experience>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}