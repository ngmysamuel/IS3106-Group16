/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.Category;
import entity.Experience;
import entity.Language;
import entity.Location;
import entity.Type;

/**
 *
 * @author samue
 */

public class CreateNewExperience {
    private String title;
    private String address;
    private Boolean active;
    private Long cat, type, location, lang, host;
            
    public CreateNewExperience(String title, String address, Boolean active) {
        this.title = title;
        this.address = address;
        this.active = active;
System.out.println("genesis: "+address);        
    }


    public CreateNewExperience() {
    }
    
    public CreateNewExperience(Experience e) {
        this.title = e.getTitle();
        this.address = e.getAddress();
        this.active = e.isActive();
        this.cat = e.getCategory().getCategoryId();
        this.type = e.getType().getTypeId();
        this.location = e.getLocation().getLocationId();
        this.lang = e.getLanguage().getLanguageId();
        this.host = e.getHost().getUserId();
    }

    public CreateNewExperience(String title, String address, Boolean active, Long cat, Long type, Long location, Long lang, Long host) {
        this.title = title;
        this.address = address;
        this.active = active;
        this.cat = cat;
        this.type = type;
        this.location = location;
        this.lang = lang;
        this.host = host;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getCat() {
        return cat;
    }

    public void setCat(Long cat) {
        this.cat = cat;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getLocation() {
        return location;
    }

    public void setLocation(Long location) {
        this.location = location;
    }

    public Long getLang() {
        return lang;
    }

    public void setLang(Long lang) {
        this.lang = lang;
    }

    public Long getHost() {
        return host;
    }

    public void setHost(Long host) {
        this.host = host;
    }

    
    
}
