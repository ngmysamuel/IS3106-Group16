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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewExperienceException;
import util.exception.DeleteExperienceException;
import util.exception.ExperienceDateNotFoundException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateEperienceInfoException;

/**
 *
 * @author samue
 */
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
    
    public ExperienceController(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    // When an experience is successfully created, it will be attached to the host's experience list
    @Override
    public Experience createNewExperience(Experience newExperience) throws CreateNewExperienceException, InputDataValidationException{
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
        if(constraintViolations.isEmpty()){
            try{
                em.persist(newExperience);
                em.flush();
                User host = newExperience.getHost();
                host.getExperienceHosted().add(newExperience);
                return newExperience;
            }catch(PersistenceException ex){
                if(ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewExperienceException("Experience with the same title already exists");
                }else{
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
                            experienceDateCancellationReportControllerLocal.createNewExperienceDateCancellationReport(rpt);
                        }
                    }
                }
            }
        } catch (ExperienceNotFoundException ex) {
            throw new DeleteExperienceException(ex.getMessage());
        }

    }
    
    @Override
    public List<Experience> retrieveAllExperiences(){
        Query query = em.createQuery("SELECT e FROM Experience e ORDER BY e.experienceId ASC");
        List<Experience> exps = query.getResultList();
        for(Experience e: exps){
            e.getExperienceDates();
        }
        return exps;
    }
    
    //Retrieve all host experiences hosted by a given user
    @Override
    public List<Experience> retrieveAllHostExperienceByHostId(Long hostUserId) { 
        Query query = em.createQuery("SELECT e FROM Experience e WHERE e.host.userId = :inHostUserId");
        query.setParameter("inHostUserId", hostUserId);

        List<Experience> hostExperiences = query.getResultList();
        return hostExperiences;
    }
    
    @Override
    public Experience retrieveExperienceById(Long id) throws ExperienceNotFoundException{
        Experience e = em.find(Experience.class, id);
        if(e == null){
            throw new ExperienceNotFoundException();
        }
        e.getExperienceDates().size();
        e.getFollowers();
        e.getProvidingItems();
        e.getRequiringItems();
        e.getReminders();
        return e;
    }
    
    @Override
    public Experience retrieveExperienceByTitle(String title) throws ExperienceNotFoundException{
        Query query = em.createQuery("SELECT e FROM Experience e WHERE e.title = :inExperienceName");
        query.setParameter("inExperienceName", title);
        if((Experience) query.getSingleResult()!=null){
            return (Experience) query.getSingleResult();
        }else{
            throw new ExperienceNotFoundException("Experience" + title + "does not exist!");
        }
    }
    
    public List<Experience> retrieveExperienceByName(String title){
        Query query = em.createQuery("SELECT e FROM Experience e WHERE e.title LIKE '%:inExperienceName5'");
        query.setParameter("inExperienceName", title);
        List<Experience> exps = query.getResultList();
        for(Experience e: exps){
            e.getExperienceDates();
        }
        return exps;
    }
    
    public List<Experience> retrieveExperienceByType(Type type){
        Query query = em.createQuery("SELECT e FROM Experience e WHERE e.type.typeId = :inTypeId ORDER BY e.experienceId DESC");
        query.setParameter("inTypeId", type.getTypeId());
        List<Experience> exps = query.getResultList();
        for(Experience e: exps){
            e.getExperienceDates();
        }
        return exps;
    }
    
    public List<Experience> retrieveExperienceByCategory(Category category){
        Query query = em.createQuery("SELECT e FROM Experience e WHERE e.category.categoryId = :inCategoryId ORDER BY e.experienceId DESC");
        query.setParameter("inCategoryId", category.getCategoryId());
        List<Experience> exps = query.getResultList();
        for(Experience e: exps){
            e.getExperienceDates();
        }
        return exps;
    }
    
    @Override
    public List<Experience> retrieveTopRatedExperience(){
        Query query = em.createQuery("SELECT DISTINCT e FROM Experience e WHERE e.host.premium = true OR e.averageScore >= 7.0 ORDER BY e.experienceId DESC");
        List<Experience> exps = query.getResultList();
        for(Experience e: exps){
            e.getExperienceDates();
        }
        return exps;
    }
    
    @Override
    public List<Experience> retrieveExperienceByLocation(Location location){
        Query query = em.createQuery("SELECT DISTINCT e FROM Experience e WHERE e.location.locationId = :inLocationId ORDER BY e.experienceId DESC");
        query.setParameter("inLocationId", location.getLocationId());
        List<Experience> exps = query.getResultList();
        for(Experience e: exps){
            e.getExperienceDates();
        }
        return exps;
    }
    
    @Override
    public List<Experience> retrieveExperienceByPrice(BigDecimal minPrice, BigDecimal maxPrice){
        List<Experience> allExperiences = this.retrieveAllExperiences();
        List<Experience> selectedExperiences = null;
        for(Experience e: allExperiences){
            if(e.getAveragePrice() != null && e.getAveragePrice().compareTo(minPrice) >= 0 && e.getAveragePrice().compareTo(maxPrice) <= 0){
                boolean add = selectedExperiences.add(e);
            }
        }
        return selectedExperiences;
    }
    
    @Override
    public List<Experience> retrieveExperienceByLanguage(Language language){
        Query query = em.createQuery("SELECT e FROM Experience e WHERE e.language.languageId = :inLanguageId ORDER BY e.experienceId DESC");
        query.setParameter("inLanguageId", language.getLanguageId());
        List<Experience> exps = query.getResultList();
        for(Experience e: exps){
            e.getExperienceDates();
        }
        return exps;
    }
    
    // this function shoud not work properly
//    @Override
//    public List<Experience> retrieveExperienceBySingleDate(Date date) {
//        Query query = em.createQuery("SELECT e FROM Experience e WHERE e.experienceDates.startDate = :inDate");
//        query.setParameter("inDate", date);
//        List<Experience> experiences = query.getResultList();
//        for(Experience e: experiences){
//            e.getExperienceDates();
//        }
//              
//        return experiences;
//    }
    
    @Override
    public List<Experience> retrieveExperienceByDate(Date startDate){
        List<Experience> allExperiences = null;
        allExperiences = this.retrieveAllExperiences();
        List<Experience> selectedExperiences = null; 
        for(Experience experience: allExperiences){
            List<ExperienceDate> dates = this.retrieveAllExperienceDates(experience);
            for(ExperienceDate date: dates){
                
                Date start = date.getStartDate();
                
                if(start.after(startDate)){
                    selectedExperiences.add(date.getExperience());
                    break;
                }
                
            }
        }
        
        return selectedExperiences; 
    }
    
    @Override
    public List<ExperienceDate> retrieveAllExperienceDates(Experience experience){
        Query query = em.createQuery("SELECT d FROM ExperienceDate d WHERE d.experience.experienceId = :inExperienceId");
        query.setParameter("inExperienceId", experience.getExperienceId());
        List<ExperienceDate> expDates = query.getResultList();
        for(ExperienceDate ed: expDates){
            ed.getPrice();
        }
        return expDates;
    }
    
    @Override
    public ExperienceDate checkExperienceDateAvailability(Long experienceId, Date date, int numOfPeople) throws ExperienceDateNotFoundException {
        Query query = em.createQuery("SELECT ed FROM ExperienceDate ed WHERE ed.experience.experienceId = :inExperienceId AND ed.startDate = :inDate");
        query.setParameter("inExperienceId", experienceId);
        query.setParameter("inDate", date);
        try{
            ExperienceDate ed = (ExperienceDate) query.getSingleResult();
            if(ed.getSpotsAvailable() < numOfPeople){
                throw new ExperienceDateNotFoundException("No slots left for the day");
            }
            return ed;
        } catch(NoResultException ex){
            throw new ExperienceDateNotFoundException("No Experience for the day");
        }
    }
    
    public Boolean removeFollowerFromExperience(Long id, User user) {
        Experience exp = em.find(Experience.class, id);
        return exp.getFollowers().remove(user);
    }

    public Boolean addFollowerToExperience(Long id, User user) {
        Experience exp = em.find(Experience.class, id);
        return exp.getFollowers().add(user);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Experience>> constraintViolations) {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }


    // filtering section
    // return a list of experiences which have at least one experience dates whose date matches the filtering date
    @Override
    public List<Experience> filterExperienceByDate(List<Experience> experienceList, Date filteringDate) {  
        ListIterator iterator = experienceList.listIterator();
        
        while(iterator.hasNext()) {
            Experience experience = (Experience)iterator.next();
            
            // retrieve the list of active experience dates of this experience
            List<ExperienceDate> activeExperienceDates = experienceDateControllerLocal.retrieveAllActiveExperienceDatesByExperienceId(experience.getExperienceId());
            List<ExperienceDate> experienceDatesMatched = new ArrayList();
            
            // remove the experience if the experience does not have any active experience date whose date matches the filtering date
            for(ExperienceDate experienceDate: activeExperienceDates) {
                if(experienceDate.getStartDate().compareTo(filteringDate) == 0) {
                    experienceDatesMatched.add(experienceDate);
                }
            }
            if(experienceDatesMatched.size() == 0) {
                iterator.remove();
            } else {
                experience.setExperienceDates(experienceDatesMatched);
            }
        }

        return experienceList;
    }
    
    // return a list of experiences which have at least one experience dates which has the number of slots available
    @Override
    public List<Experience> filterExperienceBySlotsAvailable(List<Experience> experienceList, Integer numOfPeople) {      
        ListIterator iterator = experienceList.listIterator();
        
        while(iterator.hasNext()) {
            Experience experience = (Experience)iterator.next();       
            List<ExperienceDate> availableExperienceDates = experience.getExperienceDates();
            
            ListIterator iterator2 = availableExperienceDates.listIterator();
            while(iterator2.hasNext()) {
                ExperienceDate experienceDate = (ExperienceDate)iterator2.next();
                if(experienceDate.getSpotsAvailable() < numOfPeople){
                    iterator2.remove();
                }
            }
            
            // remove the experience if it does not have any active experience date with slots available for the request
            if(availableExperienceDates.size() == 0) {
                iterator.remove();
            }
        }

        return experienceList;
    }
    
    // return a list of experiences which have at least one experience dates which has the required category
    @Override
    public List<Experience> filterExperienceByCategory(List<Experience> experienceList, Long categoryId) {

        if (categoryId != null) {
            ListIterator iterator = experienceList.listIterator();

            while (iterator.hasNext()) {
                Experience experience = (Experience) iterator.next();

                if (experience.getCategory().getCategoryId().compareTo(categoryId) != 0) {
                    iterator.remove();
                }
            }
        }

        return experienceList;
    }
}
