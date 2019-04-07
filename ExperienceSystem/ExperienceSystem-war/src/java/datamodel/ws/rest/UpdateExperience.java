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
public class UpdateExperience {
    private String title;
    private String address;
    private Boolean active;
    private Long cat, type, lang, location;
    private Long eId;

    public UpdateExperience() {
    }

    public UpdateExperience(String title, String address, Boolean active, Long cat, Long type, Long lang, Long location, Long eId) {
        this.title = title;
        this.address = address;
        this.active = active;
        this.cat = cat;
        this.type = type;
        this.lang = lang;
        this.location = location;
        this.eId = eId;
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

    public Long getLang() {
        return lang;
    }

    public void setLang(Long lang) {
        this.lang = lang;
    }

    public Long getLocation() {
        return location;
    }

    public void setLocation(Long location) {
        this.location = location;
    }

    public Long geteId() {
        return eId;
    }

    public void seteId(Long eId) {
        this.eId = eId;
    }

    
    
    
}
