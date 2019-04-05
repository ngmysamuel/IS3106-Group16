package datamodel.ws.rest;

import entity.Category;
import java.util.List;



public class RetrieveAllCategoriesRsp
{
    private List<Category> categoryEntities;

    
    
    public RetrieveAllCategoriesRsp()
    {
    }
    
    
    
    public RetrieveAllCategoriesRsp(List<Category> categoryEntities)
    {
        this.categoryEntities = categoryEntities;
    }

    
    
    public List<Category> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<Category> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }
}