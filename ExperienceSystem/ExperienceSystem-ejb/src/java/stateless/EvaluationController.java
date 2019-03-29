/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Evaluation;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author samue
 */
@Stateless
public class EvaluationController implements EvaluationControllerRemote, EvaluationControllerLocal {

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public Evaluation create(Evaluation e) {
        em.persist(e);
        em.flush();
        return e;
    }
    
    public void update(Evaluation e) {
        em.merge(e);
    }
}
