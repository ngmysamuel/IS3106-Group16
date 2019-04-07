package ws.restful;

import java.util.Set;
import javax.ws.rs.core.Application;



@javax.ws.rs.ApplicationPath("webresources")

public class ApplicationConfig extends Application 
{
    @Override
    public Set<Class<?>> getClasses() 
    {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }
    
    
    private void addRestResourceClasses(Set<Class<?>> resources) 
    {
//        resources.add(ws.restful.AppealResource.class);
//        resources.add(ws.restful.MotdResource.class);
//        resources.add(ws.restful.ProductResource.class);
//        resources.add(ws.restful.StaffResource.class);
//        resources.add(ws.restful.TagResource.class);
        resources.add(ws.restful.BookingResource.class);
//        resources.add(ws.restful.EmployeeResource.class);
//        resources.add(ws.restful.EmployeeResource.class);
        resources.add(ws.restful.EvaluationResource.class);
        resources.add(ws.restful.ExperienceResource.class);
        resources.add(ws.restful.LanguageResource.class);
        resources.add(ws.restful.UserResource.class);
        resources.add(ws.restful.CategoryResource.class);
        resources.add(ws.restful.ExperienceDateService.class);
        resources.add(ws.restful.ExperienceService.class);
        resources.add(ws.restful.LocationResource.class);
        resources.add(ws.restful.TypeResource.class);
    }    
}
