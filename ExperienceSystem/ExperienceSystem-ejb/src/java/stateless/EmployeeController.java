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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author CaiYuqian
 */
@Stateless
public class EmployeeController implements EmployeeControllerRemote, EmployeeControllerLocal {

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public EmployeeController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Employee employeeLogin(String userName, String password) throws EmployeeNotFoundException, InvalidLoginCredentialException {
        // check whether the employee exists or not
        try {
            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :inUserName");
            query.setParameter("inUserName", userName);
            Employee employee = (Employee)query.getSingleResult();
            if (employee.getPassword().equals(password)) {
                return employee;
            } else {
                throw new InvalidLoginCredentialException("Incorrect password!");
            }
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeNotFoundException("Employee " + userName + " does not exist!");
        }
    }
    
    @Override
    public Employee createNewEmployee(Employee newEmployee) throws CreateNewEmployeeException, InputDataValidationException {
        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(newEmployee);
        
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newEmployee);
                em.flush();
                return newEmployee;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewEmployeeException("Employee with the same user name already exists");
                } else {
                    throw new CreateNewEmployeeException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch (Exception ex) {
                throw new CreateNewEmployeeException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
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
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Employee>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
