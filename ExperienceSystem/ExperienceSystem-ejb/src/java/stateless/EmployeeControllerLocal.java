/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Appeal;
import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.exception.AppealNotFoundException;
import util.exception.CreateNewEmployeeException;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author CaiYuqian
 */
@Local
public interface EmployeeControllerLocal {
    public Employee employeeLogin(String userName, String password) throws EmployeeNotFoundException, InvalidLoginCredentialException;

    public Employee createNewEmployee(Employee newEmployee) throws CreateNewEmployeeException, InputDataValidationException;

    public List<Employee> retrieveAllEmployees();
}