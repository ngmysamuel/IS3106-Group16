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
public class ExperienceDateNotActiveException extends Exception {

    /**
     * Creates a new instance of <code>ExperienceDateNotActiveException</code>
     * without detail message.
     */
    public ExperienceDateNotActiveException() {
    }

    /**
     * Constructs an instance of <code>ExperienceDateNotActiveException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ExperienceDateNotActiveException(String msg) {
        super(msg);
    }
}
