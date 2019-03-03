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
public class CreateNewTypeException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewTypeException</code> without
     * detail message.
     */
    public CreateNewTypeException() {
    }

    /**
     * Constructs an instance of <code>CreateNewTypeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewTypeException(String msg) {
        super(msg);
    }
}
