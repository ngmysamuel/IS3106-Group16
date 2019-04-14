/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;
import entity.Evaluation;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewEvaluationException;
import util.exception.InputDataValidationException;

/**
 *
 * @author samue
 */
@Local
public interface EvaluationControllerLocal {
    public Evaluation createNewEvaluationFromHost(Evaluation newEvaluation, Long bookingId) throws InputDataValidationException, CreateNewEvaluationException;
    
    public List<Evaluation> retrieveAllEvaluationsFromHostsByUserId(Long userId);
    
    public List<Evaluation> retrieveAllEvaluationsFromGuestsByUserId(Long userId);
}
