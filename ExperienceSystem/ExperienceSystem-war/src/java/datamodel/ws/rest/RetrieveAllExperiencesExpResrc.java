/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.Experience;
import java.util.List;

/**
 *
 * @author samue
 */
public class RetrieveAllExperiencesExpResrc {
    private List<Experience> ls;

    public RetrieveAllExperiencesExpResrc() {
    }

    
    public RetrieveAllExperiencesExpResrc(List<Experience> ls) {
        this.ls = ls;
    }

    public List<Experience> getLs() {
        return ls;
    }

    public void setLs(List<Experience> ls) {
        this.ls = ls;
    }
    
    
}
