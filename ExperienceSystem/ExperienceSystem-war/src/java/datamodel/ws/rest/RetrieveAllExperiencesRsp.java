package datamodel.ws.rest;

import entity.Experience;
import java.util.List;



public class RetrieveAllExperiencesRsp
{
    private List<Experience> experienceEntities;

    
    
    public RetrieveAllExperiencesRsp()
    {
    }
    
    
    
    public RetrieveAllExperiencesRsp(List<Experience> experienceEntities)
    {
        this.experienceEntities = experienceEntities;
    }

    
    
    public List<Experience> getExperienceEntities() {
        return experienceEntities;
    }

    public void setExperienceEntities(List<Experience> experienceEntities) {
        this.experienceEntities = experienceEntities;
    }
}