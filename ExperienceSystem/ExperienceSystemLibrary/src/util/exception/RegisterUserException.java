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
public class RegisterUserException extends Exception {

    /**
     * Creates a new instance of <code>RegisterUserException</code> without
     * detail message.
     */
    public RegisterUserException() {
    }

    /**
     * Constructs an instance of <code>RegisterUserException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RegisterUserException(String msg) {
        super(msg);
    }
}
