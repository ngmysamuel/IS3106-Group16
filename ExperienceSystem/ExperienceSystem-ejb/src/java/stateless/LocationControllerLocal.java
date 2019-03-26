/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Location;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewLocationException;
import util.exception.DeleteLocationException;
import util.exception.InputDataValidationException;
import util.exception.LocationNotFoundException;

/**
 *
 * @author CaiYuqian
 */
@Local
public interface LocationControllerLocal {
    public Location createNewLocation(Location newLocation) throws CreateNewLocationException, InputDataValidationException;
    
    public List<Location> retrieveAllLocations();
    
    public void updateLocation(Location location) throws InputDataValidationException, LocationNotFoundException;
    
    public void deleteLocation(Long locationId) throws LocationNotFoundException, DeleteLocationException;;

    public Location retrieveLocationById(Long locationId);
}
