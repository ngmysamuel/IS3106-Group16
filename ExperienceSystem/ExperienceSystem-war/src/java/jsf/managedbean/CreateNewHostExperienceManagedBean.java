/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.primefaces.model.DualListModel;

/**
 *
 * @author CaiYuqian
 */
@Named(value = "createNewHostExperienceManagedBean")
@RequestScoped
public class CreateNewHostExperienceManagedBean {

    private List<String> itemsProvided = new ArrayList<String>();
    private List<String> itemsRequired = new ArrayList<String>();
    
    public CreateNewHostExperienceManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {

    }

    public List<String> getItemsProvided() {
        return itemsProvided;
    }

    public void setItemsProvided(List<String> itemsProvided) {
        this.itemsProvided = itemsProvided;
    }

    public List<String> getItemsRequired() {
        return itemsRequired;
    }

    public void setItemsRequired(List<String> itemsRequired) {
        this.itemsRequired = itemsRequired;
    }

    
    
}
