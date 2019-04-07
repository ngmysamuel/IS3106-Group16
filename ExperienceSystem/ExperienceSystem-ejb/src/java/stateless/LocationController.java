/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Location;
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
import util.exception.CreateNewLocationException;
import util.exception.DeleteLocationException;
import util.exception.InputDataValidationException;
import util.exception.LocationNotFoundException;

/**
 *
 * @author CaiYuqian
 */
@Stateless
public class LocationController implements LocationControllerLocal {

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public LocationController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Location createNewLocation(Location newLocation) throws CreateNewLocationException, InputDataValidationException {
        Set<ConstraintViolation<Location>> constraintViolations = validator.validate(newLocation);
        
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newLocation);
                em.flush();
                return newLocation;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewLocationException("Location with the name already exists");
                } else {
                    throw new CreateNewLocationException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch (Exception ex) {
                throw new CreateNewLocationException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<Location> retrieveAllLocations() {
        Query query = em.createQuery("SELECT l FROM Location l ORDER BY l.locationId ASC");
        return query.getResultList();
    }
    
    @Override
    public Location retrieveLocationById (Long locationId){
        return em.find(Location.class, locationId);
    }
    
    @Override
    public void updateLocation(Location location) throws InputDataValidationException, LocationNotFoundException {
        if(location.getLocationId() == null || location.getLocationId() == new Long(0)) {
            throw new InputDataValidationException("Invalid location ID!");
        }
        
        Location locationToUpdate = em.find(Location.class, location.getLocationId());
        if (locationToUpdate == null) {
            throw new LocationNotFoundException("Location does not exist!");
        }
        
        // check whether there's another Type with the same name
        Query query = em.createQuery("SELECT l FROM Location l WHERE l.location = :inLocationName");
        query.setParameter("inLocationName", location.getName());
        try{
            Location duplicateLocation = (Location)query.getSingleResult();
            if (duplicateLocation.getLocationId().equals(location.getLocationId())) {
                locationToUpdate.setName(location.getName());
            } else {
                throw new InputDataValidationException("Location with the same name already exists!");
            }
        } catch(NoResultException ex) {
            locationToUpdate.setName(location.getName());
        }
        
    }
    
    @Override
    public void deleteLocation(Long locationId) throws LocationNotFoundException, DeleteLocationException {
        Location locationToDelete = em.find(Location.class, locationId);
        if (locationToDelete == null) {
            throw new LocationNotFoundException("Location does not exist!");
        }
        
        if (locationToDelete.getExperiences().size() > 0) {
            throw new DeleteLocationException("Location cannot be deleted because there are experiences currently under that location!");
        } else {
            em.remove(locationToDelete);
        }   
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Location>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
