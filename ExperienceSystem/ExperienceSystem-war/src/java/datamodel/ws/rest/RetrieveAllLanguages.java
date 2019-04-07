/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.Language;
import java.util.List;

/**
 *
 * @author samue
 */
public class RetrieveAllLanguages {
    private List<Language> ls;

    public List<Language> getLs() {
        return ls;
    }

    public void setLs(List<Language> ls) {
        this.ls = ls;
    }

    public RetrieveAllLanguages(List<Language> ls) {
        this.ls = ls;
    }

    public RetrieveAllLanguages() {
    }
    
    
}
