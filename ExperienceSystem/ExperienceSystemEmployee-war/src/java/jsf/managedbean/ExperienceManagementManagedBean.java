/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import stateless.ExperienceControllerLocal;

/**
 *
 * @author zhangruichun
 */
@Named(value = "experienceManagementManagedBean")
@ViewScoped
public class ExperienceManagementManagedBean {

    @EJB(name = "ExperienceControllerLocal")
    private ExperienceControllerLocal experienceControllerLocal;
    
    
    
    public ExperienceManagementManagedBean() {
    }
    
}
