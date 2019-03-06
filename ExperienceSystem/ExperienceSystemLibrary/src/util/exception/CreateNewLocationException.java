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
public class CreateNewLocationException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewLocationException</code> without
     * detail message.
     */
    public CreateNewLocationException() {
    }

    /**
     * Constructs an instance of <code>CreateNewLocationException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewLocationException(String msg) {
        super(msg);
    }
}