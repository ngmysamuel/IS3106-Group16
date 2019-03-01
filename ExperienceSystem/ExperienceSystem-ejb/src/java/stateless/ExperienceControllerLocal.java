/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Experience;
import entity.User;
import javax.ejb.Local;

/**
 *
 * @author samue
 */
@Local
public interface ExperienceControllerLocal {
    public Experience createNewExperience(Experience exp);
    public void updateExperienceInformation(Experience exp);
    public void deleteExperience(Long id, String r);
    public Boolean removeFollowerFromExperience(Long id, User user);
    public Boolean addFollowerToExperience(Long id, User user);
    public Experience retrieveExperienceById(Long id);
    
}
