/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import entity.Evaluation;
import entity.Experience;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author samue
 */
@Stateless
public class EvaluationController implements EvaluationControllerLocal {

    @PersistenceContext(unitName = "ExperienceSystem-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }
    
    public Evaluation retrieveEvaluationById(Long id) {
        return em.find(Evaluation.class, id);
    }
    
    public List<Evaluation> retrieveAllEvaluations(){
        Query query = em.createQuery("SELECT e FROM Evaluation e");
        List<Evaluation> exps = query.getResultList();
        return exps;
    }

    public Evaluation create(Evaluation e) {
        em.persist(e);
        em.flush();
        return e;
    }
    
    public void delete(Long id) {
        em.remove(em.find(Evaluation.class,id));
    }
    
    public void update(Evaluation e) {
        em.merge(e);
    }
}
