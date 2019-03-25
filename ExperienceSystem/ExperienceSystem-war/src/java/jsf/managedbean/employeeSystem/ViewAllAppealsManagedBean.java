/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Appeal;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.EmployeeControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "viewAllAppealsManagedBean")
@RequestScoped
public class ViewAllAppealsManagedBean {

    @EJB(name = "EmployeeControllerLocal")
    private EmployeeControllerLocal employeeControllerLocal;

    private List<Appeal> appeals;
    
    public ViewAllAppealsManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        appeals = employeeControllerLocal.retrieveAllAppeals();
    }
    
    public void viewAppealDetails(ActionEvent event) throws IOException
    {
        Long appealIdToView = (Long)event.getComponent().getAttributes().get("appealId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("appealIdToView", appealIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAppealDetails.xhtml");
        
    }

    public List<Appeal> getAppeals() {
        return appeals;
    }

    public void setAppeals(List<Appeal> appeals) {
        this.appeals = appeals;
    }
    
    
            
    
}
