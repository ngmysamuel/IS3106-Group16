/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Employee;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.EmployeeControllerLocal;
import stateless.UserControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "accountManagementManagedBean")
@ViewScoped
public class AccountManagementManagedBean implements Serializable{

    @EJB
    private UserControllerLocal userController;

    @EJB
    private EmployeeControllerLocal employeeControllerLocal;

    private Employee currentEmployee;
    private Long currentEmployeeId;
    
    public AccountManagementManagedBean() {
    }
    
    @PostConstruct 
    public void postConstruct(){
        System.out.println("Try to access account information");
        currentEmployee = (Employee) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentEmployee");
        currentEmployeeId = currentEmployee.getEmployeeId();
    }

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    public Long getCurrentEmployeeId() {
        return currentEmployeeId;
    }

    public void setCurrentEmployeeId(Long currentEmployeeId) {
        this.currentEmployeeId = currentEmployeeId;
    }
    
    
    
}
