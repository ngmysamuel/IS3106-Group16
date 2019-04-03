/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author CaiYuqian
 */
public class DeleteExperienceException extends Exception {

    /**
     * Creates a new instance of <code>DeleteExperienceException</code> without
     * detail message.
     */
    public DeleteExperienceException() {
    }

    /**
     * Constructs an instance of <code>DeleteExperienceException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteExperienceException(String msg) {
        super(msg);
    }
}
