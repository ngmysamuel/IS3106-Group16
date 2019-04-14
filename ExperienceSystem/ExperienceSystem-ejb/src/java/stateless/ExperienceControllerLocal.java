/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Experience;
import entity.User;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewExperienceException;
import util.exception.DeleteExperienceException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateEperienceInfoException;


@Local
public interface ExperienceControllerLocal {

    // Experience CRUD
    public Experience createNewExperience(Experience newExperience) throws CreateNewExperienceException, InputDataValidationException;
    public void updateExperienceInformation(Experience experience) throws UpdateEperienceInfoException;
    public void deleteExperience(Long id) throws DeleteExperienceException;

    // retrieving
    public Experience retrieveExperienceById(Long experienceId) throws ExperienceNotFoundException;
    public List<Experience> retrieveAllExperiences();  
    public List<Experience> retrieveAllHostExperiencesByHostId(Long hostUserId);
    public List<Experience> retrieveAllActiveHostExperiencesByHostId(Long hostUserId);
    public List<Experience> retrieveFavouriteExperiences(Long userId);
    public List<Experience> retrievePastExperiences(Long userId);
    public List<Experience> retrieveUpcomingExperiences(Long userId);
    public List<Experience> retrieveTopRatedExperience();
    
    // experience followers
    public void removeFollowerFromExperience(Long experienceId, Long userId);
    public void addFollowerToExperience(Long experienceId, Long userId);
    public List<User> retrieveExperienceFollowers(Long experienceId);

    // filtering 
    public List<Experience> filterExperienceByActiveState(List<Experience> experienceList);
    public List<Experience> filterExperienceByDate(List<Experience> experienceList, Date filteringDate);
    public List<Experience> filterExperienceBySlotsAvailable(List<Experience> experienceList, Integer numOfPeople);
    public List<Experience> filterExperienceByCategory(List<Experience> experienceList, Long categoryId);
    public List<Experience> filterExperienceByType(List<Experience> experienceList, Long typeId);
    public List<Experience> filterExperienceByLanguage(List<Experience> experienceList, Long languageId);
    public List<Experience> filterExperienceByLocation(List<Experience> experienceList, Long locationId);
    
}