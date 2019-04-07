/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;
import entity.Evaluation;
import entity.Experience;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author samue
 */
@Local
public interface EvaluationControllerLocal {
    public Evaluation retrieveEvaluationById(Long id);
    public List<Evaluation> retrieveAllEvaluations();
    public Evaluation create(Evaluation e);
    public void delete(Long id);
    public void update(Evaluation e);
}
