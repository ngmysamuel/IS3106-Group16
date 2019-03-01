/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Experience;
import entity.ExperienceDate;
import javax.ejb.Local;

/**
 *
 * @author samue
 */
@Local
public interface ExperienceDateControllerLocal {
    public ExperienceDate createExperienceDate(ExperienceDate expDate, Experience exp);
    public void updateExperienceDate(ExperienceDate expDate);
    public ExperienceDate retrieveExperienceDateByDateId(Long id);
    public void deleteExperienceDate(Long id, String r);
}
