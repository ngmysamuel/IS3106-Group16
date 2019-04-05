package datamodel.ws.rest;

import entity.Type;
import java.util.List;



public class RetrieveAllTypesRsp
{
    private List<Type> typeEntities;

    
    
    public RetrieveAllTypesRsp()
    {
    }
    
    
    
    public RetrieveAllTypesRsp(List<Type> typeEntities)
    {
        this.typeEntities = typeEntities;
    }

    
    
    public List<Type> getTypeEntities() {
        return typeEntities;
    }

    public void setTypeEntities(List<Type> typeEntities) {
        this.typeEntities = typeEntities;
    }
}