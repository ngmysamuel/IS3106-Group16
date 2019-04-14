/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Appeal;
import entity.Employee;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AppealNotFoundException;
import util.exception.CreateNewEmployeeException;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author zhangruichun
 */
@Stateless
public class AppealController implements AppealControllerLocal {
    
    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AppealController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public Appeal createNewAppeal(Appeal appeal) throws InputDataValidationException{
        Set<ConstraintViolation<Appeal>> constraintViolations = validator.validate(appeal);
        if(constraintViolations.isEmpty()){
            em.persist(appeal);
            em.flush();
            return appeal;
        }else{
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<Appeal> retrieveAllAppeals() {
        Query query = em.createQuery("SELECT a FROM Appeal a ORDER BY a.appealId DESC");
        return query.getResultList();
    }
    
    @Override
    public Appeal retrieveAppealById(Long appealId){
        return em.find(Appeal.class, appealId);
    }
    
    public void processAppeal(Long appealId, String reply, Long employeeId) throws AppealNotFoundException, EmployeeNotFoundException {
        Appeal appeal = em.find(Appeal.class, appealId);
        if (appeal == null) {
            throw new AppealNotFoundException("Appeal with ID " + appealId + " does not exist!");
        }
        
        Employee employee = em.find(Employee.class, employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " does not exist!");
        }
        
        appeal.setReply(reply);
        appeal.setEmployee(employee);
        appeal.setIsProcessed(true);
        employee.getAppeals().add(appeal);
        
        // TODO: send notification to the user that the appeal has been processed
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Appeal>> constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
