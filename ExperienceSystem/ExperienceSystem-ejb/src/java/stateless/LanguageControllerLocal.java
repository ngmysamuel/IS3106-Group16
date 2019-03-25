/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Language;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewLanguageException;
import util.exception.DeleteLanguageException;
import util.exception.InputDataValidationException;
import util.exception.LanguageNotFoundException;

/**
 *
 * @author CaiYuqian
 */
@Local
public interface LanguageControllerLocal {
    public Language createNewLanguage(Language newLanguage) throws CreateNewLanguageException, InputDataValidationException;
    
    public List<Language> retrieveAllLanguages();
    
    public void updateLanguage(Language language) throws InputDataValidationException, LanguageNotFoundException;
    
    public void deleteLanguage(Long languageId) throws LanguageNotFoundException, DeleteLanguageException;;

    public Language retrieveLanguageById(Long languageId);
}
