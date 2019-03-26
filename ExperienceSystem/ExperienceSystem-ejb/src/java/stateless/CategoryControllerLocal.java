/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Category;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCatgeoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;

/**
 *
 * @author CaiYuqian
 */
@Local
public interface CategoryControllerLocal {
    public Category createNewCategory(Category newCategory) throws CreateNewCatgeoryException, InputDataValidationException;
    
    public List<Category> retrieveAllCategories();
    
    public Category retrieveCategoryById (Long categoryId);
    
    public void updateCategory(Category category) throws InputDataValidationException, CategoryNotFoundException;
    
    public void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException;;

}
