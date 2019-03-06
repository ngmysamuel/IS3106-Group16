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
public class ExperienceDateNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ExperienceDateNotFoundException</code>
     * without detail message.
     */
    public ExperienceDateNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ExperienceDateNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ExperienceDateNotFoundException(String msg) {
        super(msg);
    }
}
