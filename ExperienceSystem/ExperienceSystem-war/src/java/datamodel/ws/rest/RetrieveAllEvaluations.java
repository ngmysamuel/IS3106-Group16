/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.Evaluation;
import java.util.List;

/**
 *
 * @author samue
 */
public class RetrieveAllEvaluations {
    private List<Evaluation> ls;

    public RetrieveAllEvaluations() {
    }

    public RetrieveAllEvaluations(List<Evaluation> ls) {
        this.ls = ls;
    }

    public List<Evaluation> getLs() {
        return ls;
    }

    public void setLs(List<Evaluation> ls) {
        this.ls = ls;
    }

    
    
}
