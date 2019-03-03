/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Type;
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
import util.exception.CreateNewTypeException;
import util.exception.DeleteTypeException;
import util.exception.InputDataValidationException;
import util.exception.TypeNotFoundException;

/**
 *
 * @author CaiYuqian
 */
@Stateless
public class TypeController implements TypeControllerRemote, TypeControllerLocal {

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public TypeController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Type createNewType(Type newType) throws CreateNewTypeException, InputDataValidationException {
        Set<ConstraintViolation<Type>> constraintViolations = validator.validate(newType);
        
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newType);
                em.flush();
                return newType;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewTypeException("Type with the name already exists");
                } else {
                    throw new CreateNewTypeException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch (Exception ex) {
                throw new CreateNewTypeException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<Type> retrieveAllTypes() {
        Query query = em.createQuery("SELECT t FROM Type t ORDER BY t.typeId ASC");
        return query.getResultList();
    }
    
    @Override
    public void updateType(Type type) throws InputDataValidationException, TypeNotFoundException {
        if(type.getTypeId() == null || type.getTypeId() == new Long(0)) {
            throw new InputDataValidationException("Invalid type ID!");
        }
        
        Type typeToUpdate = em.find(Type.class, type.getTypeId());
        if (typeToUpdate == null) {
            throw new TypeNotFoundException("Type does not exist!");
        }
        
        // check whether there's another Type with the same name
        Query query = em.createQuery("SELECT t FROM Type t WHERE t.type = :inTypeName");
        query.setParameter("inTypeName", type.getType());
        try{
            Type duplicateType = (Type)query.getSingleResult();
            if (duplicateType.getTypeId().equals(type.getTypeId())) {
                typeToUpdate.setType(type.getType());
            } else {
                throw new InputDataValidationException("Type with the same name already exists!");
            }
        } catch(NoResultException ex) {
            typeToUpdate.setType(type.getType());
        }
        
    }
    
    @Override
    public void deleteType(Long typeId) throws TypeNotFoundException, DeleteTypeException {
        Type typeToDelete = em.find(Type.class, typeId);
        if (typeToDelete == null) {
            throw new TypeNotFoundException("Type does not exist!");
        }
        
        if (typeToDelete.getExperiences().size() > 0) {
            throw new DeleteTypeException("Type cannot be deleted because there are experiences currently under that type!");
        } else {
            em.remove(typeToDelete);
        }   
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Type>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
