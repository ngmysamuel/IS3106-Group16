/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Experience;
import entity.ExperienceDate;
import entity.User;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewExperienceDateException;
import util.exception.CreateNewExperienceException;
import util.exception.DeleteExperienceDateException;
import util.exception.ExperienceDateNotActiveException;
import util.exception.ExperienceDateNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author samue
 */
@Local
public interface ExperienceDateControllerLocal {
    
    public ExperienceDate createNewExperienceDate(ExperienceDate newExperienceDate) throws CreateNewExperienceDateException, InputDataValidationException;
    
    public ExperienceDate retrieveExperienceDateByExperienceDateId(Long experienceDateId)throws ExperienceDateNotFoundException;
    public List<ExperienceDate> retrieveExperienceDatesOfAnExperience(Experience experience);
    public List<ExperienceDate> retrieveAllActiveExperienceDatesByExperienceId(Long experienceId);
    public List<ExperienceDate> retrieveAllInactiveExperienceDatesByExperienceId(Long experienceId);
    public List<User> retrieveAllGuestsByExperienceDateId(Long experienceDateId);
    
    public ExperienceDate updateExperienceDate(ExperienceDate expDate)throws InputDataValidationException, CreateNewExperienceException;

    public void deleteExperienceDate(Long id, String r) throws ExperienceDateNotActiveException, DeleteExperienceDateException;
  
}