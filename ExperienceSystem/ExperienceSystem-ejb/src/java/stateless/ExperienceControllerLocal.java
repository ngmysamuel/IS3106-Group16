/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Category;
import entity.Experience;
import entity.ExperienceDate;
import entity.Language;
import entity.Location;
import entity.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewExperienceException;
import util.exception.DeleteExperienceException;
import util.exception.ExperienceDateNotFoundException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateEperienceInfoException;
import util.exception.UserNotFoundException;

/**
 *
 * @author samue
 */
@Local
public interface ExperienceControllerLocal {

    // Experience CRUD
    public Experience createNewExperience(Experience newExperience) throws CreateNewExperienceException, InputDataValidationException;
    public void updateExperienceInformation(Experience experience) throws UpdateEperienceInfoException;
    public void deleteExperience(Long id) throws DeleteExperienceException;

    // retrieving
    public Experience retrieveExperienceById(Long id) throws ExperienceNotFoundException;
    public List<Experience> retrieveAllExperiences();  
    public List<Experience> retrieveAllHostExperienceByHostId(Long hostUserId);
    public Experience retrieveExperienceByTitle(String title) throws ExperienceNotFoundException;
    public List<Experience> retrieveTopRatedExperience();
    public List<Experience> retrieveExperienceByLocation(Location location);
    public List<Experience> retrieveExperienceByPrice(BigDecimal minPrice, BigDecimal maxPrice);
    public List<Experience> retrieveExperienceByType(Type type);
    public List<Experience> retrieveExperienceByLanguage(Language language);
    public List<Experience> retrieveExperienceByDate(Date startDate);
    public List<Experience> retrieveExperienceByCategory(Category category);
    public List<Experience> retrieveExperienceByName(String title);
    
    // experience followers
    public void removeFollowerFromExperience(Long experienceId, Long userId) throws UserNotFoundException, ExperienceNotFoundException;
    public void addFollowerToExperience(Long experienceId, Long userId) throws UserNotFoundException, ExperienceNotFoundException;

    // filtering 
    public List<Experience> filterExperienceByActiveState(List<Experience> experienceList);
    public List<Experience> filterExperienceByDate(List<Experience> experienceList, Date filteringDate);
    public List<Experience> filterExperienceBySlotsAvailable(List<Experience> experienceList, Integer numOfPeople);
    public List<Experience> filterExperienceByCategory(List<Experience> experienceList, Long categoryId);
    public List<Experience> filterExperienceByType(List<Experience> experienceList, Long typeId);
    public List<Experience> filterExperienceByLanguage(List<Experience> experienceList, Long languageId);
    public List<Experience> filterExperienceByLocation(List<Experience> experienceList, Long locationId);
    
    public List<ExperienceDate> retrieveAllExperienceDates(Experience experience);

    ExperienceDate checkExperienceDateAvailability(Long experienceId, Date date, int numOfPeople) throws ExperienceDateNotFoundException;

    public List<Experience> retrieveFavoriteExperiences(Long userId);

    public List<Experience> retrievePastExperiences(Long userId);

}
