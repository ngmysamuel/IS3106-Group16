/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Category;
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
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCatgeoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;

/**
 *
 * @author CaiYuqian
 */
@Stateless
public class CategoryController implements CategoryControllerRemote, CategoryControllerLocal {

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CategoryController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Category createNewCategory(Category newCategory) throws CreateNewCatgeoryException, InputDataValidationException {
        Set<ConstraintViolation<Category>> constraintViolations = validator.validate(newCategory);
        
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newCategory);
                em.flush();
                return newCategory;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewCatgeoryException("Catgeory with the name already exists");
                } else {
                    throw new CreateNewCatgeoryException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch (Exception ex) {
                throw new CreateNewCatgeoryException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<Category> retrieveAllCategories() {
        Query query = em.createQuery("SELECT c FROM Category c ORDER BY c.categoryId ASC");
        return query.getResultList();
    }
    
    @Override
    public Category retrieveCategoryById (Long categoryId){
        return em.find(Category.class, categoryId);
    }
    @Override
    public void updateCategory(Category category) throws InputDataValidationException, CategoryNotFoundException {
        if(category.getCategoryId() == null || category.getCategoryId() == new Long(0)) {
            throw new InputDataValidationException("Invalid category ID!");
        }
        
        Category categoryToUpdate = em.find(Category.class, category.getCategoryId());
        if (categoryToUpdate == null) {
            throw new CategoryNotFoundException("Category does not exist!");
        }
        
        // check whether there's another Type with the same name
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.category = :inCategoryName");
        query.setParameter("inCategoryName", category.getCategory());
        try{
            Category duplicateCategory = (Category)query.getSingleResult();
            if (duplicateCategory.getCategoryId().equals(category.getCategoryId())) {
                categoryToUpdate.setCategory(category.getCategory());
                categoryToUpdate.setDescription(category.getDescription());
            } else {
                throw new InputDataValidationException("Category with the same name already exists!");
            }
        } catch(NoResultException ex) {
            categoryToUpdate.setCategory(category.getCategory());
            categoryToUpdate.setDescription(category.getDescription());
        }
        
    }
    
    @Override
    public void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException {
        Category categoryToDelete = em.find(Category.class, categoryId);
        if (categoryToDelete == null) {
            throw new CategoryNotFoundException("Category does not exist!");
        }
        
        if (categoryToDelete.getExperiences().size() > 0) {
            throw new DeleteCategoryException("Category cannot be deleted because there are experiences currently under that category!");
        } else {
            em.remove(categoryToDelete);
        }   
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Category>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
