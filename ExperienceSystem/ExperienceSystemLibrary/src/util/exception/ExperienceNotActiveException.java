/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author zhangruichun
 */
public class ExperienceNotActiveException extends Exception {

    /**
     * Creates a new instance of <code>ExperienceNotActiveException</code>
     * without detail message.
     */
    public ExperienceNotActiveException() {
    }

    /**
     * Constructs an instance of <code>ExperienceNotActiveException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ExperienceNotActiveException(String msg) {
        super(msg);
    }
}
