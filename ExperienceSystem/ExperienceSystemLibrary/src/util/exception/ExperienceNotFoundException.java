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
public class ExperienceNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ExperienceNotFoundException</code>
     * without detail message.
     */
    public ExperienceNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ExperienceNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ExperienceNotFoundException(String msg) {
        super(msg);
    }
}
