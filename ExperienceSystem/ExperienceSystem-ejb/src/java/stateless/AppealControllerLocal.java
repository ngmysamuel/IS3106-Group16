/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Appeal;
import java.util.List;
import javax.ejb.Local;
import util.exception.AppealNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author zhangruichun
 */
@Local
public interface AppealControllerLocal {

    public List<Appeal> retrieveAllAppeals();

    public Appeal retrieveAppealById(Long appealId);

    public void processAppeal(Long appealId, String reply, Long employeeId) throws AppealNotFoundException, EmployeeNotFoundException;

    public Appeal createNewAppeal(Appeal appeal) throws InputDataValidationException;
    
}
