/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author CaiYuqian
 */
@Named(value = "viewExperienceDetailsManagedBean")
@ViewScoped
public class ViewExperienceDetailsManagedBean implements Serializable {

    private List<String> images;
    
    public ViewExperienceDetailsManagedBean() {
        images = new ArrayList();
        images.add("https://i.imgur.com/2hocHvd.jpg");
        images.add("https://i.imgur.com/R6wtAtN.jpg");
        images.add("https://i.imgur.com/slbHL1Z.jpg");
        images.add("https://i.imgur.com/SQiLyd4.jpg");
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
    
}
