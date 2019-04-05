package datamodel.ws.rest;

import entity.Location;
import java.util.List;



public class RetrieveAllLocationsRsp
{
    private List<Location> locationEntities;

    
    
    public RetrieveAllLocationsRsp()
    {
    }
    
    
    
    public RetrieveAllLocationsRsp(List<Location> locationEntities)
    {
        this.locationEntities = locationEntities;
    }

    
    
    public List<Location> getLocationEntities() {
        return locationEntities;
    }

    public void setLocationEntities(List<Location> locationEntities) {
        this.locationEntities = locationEntities;
    }
}