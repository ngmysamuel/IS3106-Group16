/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Asus
 */
public class CreateNewEvaluationException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewEvaluationException</code>
     * without detail message.
     */
    public CreateNewEvaluationException() {
    }

    /**
     * Constructs an instance of <code>CreateNewEvaluationException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewEvaluationException(String msg) {
        super(msg);
    }
}
