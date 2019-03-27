/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Experience;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.event.ActionEvent;
import javax.inject.Named;
import javax.faces.bean.ViewScoped;


@Named(value = "hostExperienceManagementManagedBean")
@ViewScoped
public class HostExperienceManagementManagedBean implements Serializable {

    private Experience experienceToView;
    private List<String> images;
    
    private List<String> itemsProvided;
    private List<String> itemsRequired;
    
    private Date updateExperienceDate;
    
    public HostExperienceManagementManagedBean() {
        experienceToView = new Experience("Experience one", "Desc1");
        images = new ArrayList();
        images.add("https://i.imgur.com/2hocHvd.jpg");
        images.add("https://i.imgur.com/R6wtAtN.jpg");
        images.add("https://i.imgur.com/slbHL1Z.jpg");
        images.add("https://i.imgur.com/SQiLyd4.jpg");
    }
    
    public void back(ActionEvent event) {
        
    }

    public Experience getExperienceToView() {
        return experienceToView;
    }

    public void setExperienceToView(Experience experienceToView) {
        this.experienceToView = experienceToView;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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

    public Date getUpdateExperienceDate() {
        return updateExperienceDate;
    }

    public void setUpdateExperienceDate(Date updateExperienceDate) {
        this.updateExperienceDate = updateExperienceDate;
    }

}
