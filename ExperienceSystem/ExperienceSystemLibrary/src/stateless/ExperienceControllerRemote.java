/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.ExperienceDate;
import java.util.Date;
import javax.ejb.Remote;
import util.exception.ExperienceDateNotFoundException;

/**
 *
 * @author samue
 */
@Remote
public interface ExperienceControllerRemote {

    ExperienceDate checkExperienceDateAvailability(Long experienceId, Date date, int numOfPeople) throws ExperienceDateNotFoundException;
    
}
