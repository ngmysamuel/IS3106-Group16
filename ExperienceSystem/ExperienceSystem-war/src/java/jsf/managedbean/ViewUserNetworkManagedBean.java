/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "viewUserNetworkManagedBean")
@ViewScoped
public class ViewUserNetworkManagedBean implements Serializable {

    
    private List<User> followers;
    
    public ViewUserNetworkManagedBean() {
        followers = new ArrayList();
        followers.add(new User());
        followers.add(new User());
        followers.add(new User());
        followers.add(new User());
        followers.add(new User());
        followers.add(new User());
        followers.add(new User());
        followers.add(new User());
        followers.add(new User());
        followers.add(new User());
        followers.add(new User());
        followers.add(new User());
        followers.add(new User());
        followers.add(new User());
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }
    
}
