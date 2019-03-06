/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Booking;
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
import java.util.Date;
import java.util.List;
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
import util.exception.ExperienceNotActiveException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author samue
 */
@Stateless
public class ExperienceController implements ExperienceControllerRemote, ExperienceControllerLocal {

    @EJB
    private ExperienceDateCancellationReportControllerLocal experienceDateCancellationReportController;

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public ExperienceController(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Experience createNewExperience(Experience newExperience) throws CreateNewExperienceException, InputDataValidationException{
        Set<ConstraintViolation<Experience>> constraintViolations = validator.validate(newExperience);
        if(constraintViolations.isEmpty()){
            try{
                em.persist(newExperience);
                em.flush();
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
    public void updateExperienceInformation(Experience experience) throws InputDataValidationException, ExperienceNotFoundException{
        if(experience.getExperienceId() == null || experience.getExperienceId() == new Long (0)) {
            throw new InputDataValidationException("Invalid experience ID");
        }
        
        Experience experienceToUpdate = em.find(Experience.class, experience.getExperienceId());
        if(experienceToUpdate == null){
            throw new ExperienceNotFoundException("Experience does not exist!");
        }
        
        // check whether there's another experience with the same title as new experience title 
        Query query = em.createQuery("SELECT e FROM Experience e where e.experience = :inExperienceName");
        query.setParameter("inExperienceName", experience.getTitle());
        
        try{
            Experience duplicateExperience = (Experience)query.getSingleResult();
            // if no same title 
            if (duplicateExperience.getExperienceId().equals(experience.getExperienceId())) {
                experienceToUpdate.setTitle(experience.getTitle());
            } else {
                throw new InputDataValidationException("Experience with the same title already exists!");
            }
        } catch(NoResultException ex) {
            experienceToUpdate.setTitle(experience.getTitle());
        }
    }
    
    @Override
    public void deleteExperience(Long id, String r) throws ExperienceNotActiveException{
        Experience experience = em.find(Experience.class, id);
        if(!experience.isActive()){
            throw new ExperienceNotActiveException("This experience is no longer active!");
        }else{    
            experience.setActive(false);
        
            LocalDate current = LocalDate.now();
            
            List<ExperienceDate> experienceDates = experience.getExperienceDates();
            for (ExperienceDate date : experienceDates) {
                if (date.getStartDate().compareTo(current) > 0) {
                    List<Booking> bookings = date.getBookings();
                    for (Booking b : bookings) {
                        b.setStatus(StatusEnum.CANCELLED);
                        ExperienceDateCancellationReport rpt = new ExperienceDateCancellationReport();
                        rpt.setBooking(b);
                        rpt.setExperienceDate(date);
                        rpt.setCancellationReason(r);
                        experienceDateCancellationReportController.createNewExperienceDateCancellationReport(rpt);
                    }
                }
            }
        }
    }
    
    @Override
    public List<Experience> retrieveAllExperiences(){
        Query query = em.createQuery("SELECT e FROM Experience e ORDER BY e.experienceId ASC");
        return query.getResultList();
    }
    
    @Override
    public Experience retrieveExperienceById(Long id) {
        return em.find(Experience.class, id);
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
    
    public List<Experience> retrieveExperienceByType(Type type){
        Query query = em.createQuery("SELECT e FROM Experience e WHERE e.type.typeId = :inTypeId ORDER BY e.experienceId DESC");
        query.setParameter("inTypeId", type.getTypeId());
        return query.getResultList();
    }
    
    @Override
    public List<Experience> retrieveTopRatedExperience(){
        Query query = em.createQuery("SELECT DISTINCT e FROM Experience e WHERE e.host.premium = true OR e.averageScore >= 7.0 ORDER BY e.experienceId DESC");
        return query.getResultList();
    }
    
    @Override
    public List<Experience> retrieveExperienceByLocation(Location location){
        Query query = em.createQuery("SELECT DISTINCT e FROM Experience e WHERE e.location.locationId = :inLocationId ORDER BY e.experienceId DESC");
        query.setParameter("inLocationId", location.getLocationId());
        return query.getResultList();
    }
    
    @Override
    public List<Experience> retrieveExperienceByPrice(BigDecimal minPrice, BigDecimal maxPrice){
        List<Experience> allExperiences = this.retrieveAllExperiences();
        List<Experience> selectedExperiences = null;
        for(Experience e: allExperiences){
            if(this.getAveragePrice(e).compareTo(minPrice) >= 0 && this.getAveragePrice(e).compareTo(maxPrice) <= 0){
                boolean add = selectedExperiences.add(e);
            }
        }
        return selectedExperiences;
    }
    
    @Override
    public BigDecimal getAveragePrice(Experience experience){
        Query query = em.createQuery("SELECT d FROM ExperienceDate d WHERE d.experience.experienceId = :inExperienceId");
        query.setParameter("inExperienceId", experience.getExperienceId());
        List<ExperienceDate> dates = query.getResultList();
        
        BigDecimal total = new BigDecimal('0');
        int count = dates.size();
        for(ExperienceDate date : dates){
            total.add(date.getPrice());
        }
        BigDecimal num = new BigDecimal(count);
        
        return total.divide(num);
    }
    
    @Override
    public List<Experience> retrieveExperienceByLanguage(Language language){
        Query query = em.createQuery("SELECT e FROM Experience e WHERE e.language.languageId = :inLanguageId ORDER BY e.experienceId DESC");
        query.setParameter("inLanguageId", language.getLanguageId());
        return query.getResultList();
    }
    
    @Override
    public List<Experience> retrieveExperienceByDate(Date startDate, Date endDate){
        List<Experience> allExperiences = null;
        allExperiences = this.retrieveAllExperiences();
        List<Experience> selectedExperiences = null; 
        for(Experience experience: allExperiences){
            List<ExperienceDate> dates = this.retrieveAllExperienceDates(experience);
            for(ExperienceDate date: dates){
                
                Date start = java.sql.Date.valueOf(date.getStartDate());
                Date end = java.sql.Date.valueOf(date.getEndDate());
                
                if(start.after(startDate) && end.before(endDate)){
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
        return query.getResultList();
    }
    
    
    public Boolean removeFollowerFromExperience(Long id, User user) {
        Experience exp = em.find(Experience.class, id);
        return exp.getFollowers().remove(user);
    }

    public Boolean addFollowerToExperience(Long id, User user) {
        Experience exp = em.find(Experience.class, id);
        return exp.getFollowers().add(user);
    }

    

    public void persist(Object object) {
        em.persist(object);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Experience>> constraintViolations) {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

}
