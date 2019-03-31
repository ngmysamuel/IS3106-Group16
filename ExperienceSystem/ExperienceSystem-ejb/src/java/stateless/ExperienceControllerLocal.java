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
import entity.User;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.validation.ConstraintViolationException;
import util.exception.CreateNewExperienceException;
import util.exception.ExperienceDateNotFoundException;
import util.exception.ExperienceNotActiveException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author samue
 */
@Local
public interface ExperienceControllerLocal {

    public List<User> retrieveAllUsers(Experience exp);
    
    public Boolean removeFollowerFromExperience(Long id, User user);
    public Boolean addFollowerToExperience(Long id, User user);
    public Experience retrieveExperienceById(Long id) throws ExperienceNotFoundException;

    public Experience createNewExperience(Experience newExperience) throws CreateNewExperienceException, InputDataValidationException;
    public Experience createExpWithLangTypeCat(Experience exp, Long catId, Long typeId, Long langId) throws CreateNewExperienceException, InputDataValidationException;
    public void updateExperienceWithCatTypeLang(Experience exp, Long catId, Long typeId, Long langId) throws InputDataValidationException;
    public void updateExperienceInformation(Experience experience) throws ConstraintViolationException, InputDataValidationException, ExperienceNotFoundException;

    public void deleteExperience(Long id, String r) throws ExperienceNotActiveException;

    public List<Experience> retrieveAllExperiences();

    public Experience retrieveExperienceByTitle(String title) throws ExperienceNotFoundException;

    public List<Experience> retrieveTopRatedExperience();

    public List<Experience> retrieveExperienceByLocation(Location location);

    public BigDecimal getAveragePrice(Experience experience);

    public List<Experience> retrieveExperienceByPrice(BigDecimal minPrice, BigDecimal maxPrice);

    public List<Experience> retrieveExperienceByType(Type type);

    public List<Experience> retrieveExperienceByLanguage(Language language);

    public List<ExperienceDate> retrieveAllExperienceDates(Experience experience);

    public List<Experience> retrieveExperienceByDate(Date startDate);

    public List<Experience> retrieveExperienceByCategory(Category category);

    public List<Experience> retrieveExperienceByName(String title);

    List<Experience> retrieveExperienceBySingleDate(Date date);
    ExperienceDate checkExperienceDateAvailability(Long experienceId, Date date, int numOfPeople) throws ExperienceDateNotFoundException;
    
}
