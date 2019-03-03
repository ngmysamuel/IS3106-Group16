/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Type;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewTypeException;
import util.exception.DeleteTypeException;
import util.exception.InputDataValidationException;
import util.exception.TypeNotFoundException;

/**
 *
 * @author CaiYuqian
 */
@Local
public interface TypeControllerLocal {
    public Type createNewType(Type newType) throws CreateNewTypeException, InputDataValidationException;
    
    List<Type> retrieveAllTypes();
    
    public void updateType(Type type) throws InputDataValidationException, TypeNotFoundException;
    
    public void deleteType(Long typeId) throws TypeNotFoundException, DeleteTypeException;
}
