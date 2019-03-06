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
public class CreateNewExperienceException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewExperienceException</code>
     * without detail message.
     */
    public CreateNewExperienceException() {
    }

    /**
     * Constructs an instance of <code>CreateNewExperienceException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewExperienceException(String msg) {
        super(msg);
    }
}
