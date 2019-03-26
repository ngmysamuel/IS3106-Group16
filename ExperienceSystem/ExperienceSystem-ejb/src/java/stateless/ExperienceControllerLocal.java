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
import util.exception.CreateNewExperienceException;
import util.exception.ExperienceNotActiveException;
import util.exception.ExperienceNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author samue
 */
@Local
public interface ExperienceControllerLocal {

    
    public Boolean removeFollowerFromExperience(Long id, User user);
    public Boolean addFollowerToExperience(Long id, User user);
    public Experience retrieveExperienceById(Long id) throws ExperienceNotFoundException;

    public Experience createNewExperience(Experience newExperience) throws CreateNewExperienceException, InputDataValidationException;

    public void updateExperienceInformation(Experience experience) throws InputDataValidationException, ExperienceNotFoundException;

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

    public List<Experience> retrieveExperienceByDate(Date startDate, Date endDate);

    public List<Experience> retrieveExperienceByCategory(Category category);

    public List<Experience> retrieveExperienceByName(String title);
    
}
