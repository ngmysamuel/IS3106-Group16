/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Language;
import java.util.List;
import java.util.Set;
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
import util.exception.CreateNewLanguageException;
import util.exception.DeleteLanguageException;
import util.exception.InputDataValidationException;
import util.exception.LanguageNotFoundException;

/**
 *
 * @author CaiYuqian
 */
@Stateless
public class LanguageController implements LanguageControllerRemote, LanguageControllerLocal {

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public LanguageController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Language createNewLanguage(Language newLanguage) throws CreateNewLanguageException, InputDataValidationException {
        Set<ConstraintViolation<Language>> constraintViolations = validator.validate(newLanguage);
        
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newLanguage);
                em.flush();
                return newLanguage;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewLanguageException("Language with the name already exists");
                } else {
                    throw new CreateNewLanguageException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch (Exception ex) {
                throw new CreateNewLanguageException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<Language> retrieveAllLanguages() {
        Query query = em.createQuery("SELECT l FROM Language l ORDER BY l.languageId ASC");
        return query.getResultList();
    }
    
    @Override
    public void updateLanguage(Language language) throws InputDataValidationException, LanguageNotFoundException {
        if(language.getLanguageId() == null || language.getLanguageId() == new Long(0)) {
            throw new InputDataValidationException("Invalid language ID!");
        }
        
        Language languageToUpdate = em.find(Language.class, language.getLanguageId());
        if (languageToUpdate == null) {
            throw new LanguageNotFoundException("Language does not exist!");
        }
        
        // check whether there's another Type with the same name
        Query query = em.createQuery("SELECT l FROM Language l WHERE l.language = :inLanguageName");
        query.setParameter("inLanguageName", language.getLanguage());
        try{
            Language duplicateLanguage = (Language)query.getSingleResult();
            if (duplicateLanguage.getLanguageId().equals(language.getLanguageId())) {
                languageToUpdate.setLanguage(language.getLanguage());
            } else {
                throw new InputDataValidationException("Language with the same name already exists!");
            }
        } catch(NoResultException ex) {
            languageToUpdate.setLanguage(language.getLanguage());
        }
        
    }
    
    @Override
    public void deleteLanguage(Long languageId) throws LanguageNotFoundException, DeleteLanguageException {
        Language languageToDelete = em.find(Language.class, languageId);
        if (languageToDelete == null) {
            throw new LanguageNotFoundException("Language does not exist!");
        }
        
        if (languageToDelete.getExperiences().size() > 0) {
            throw new DeleteLanguageException("Language cannot be deleted because there are experiences currently under that language!");
        } else {
            em.remove(languageToDelete);
        }   
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Language>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
