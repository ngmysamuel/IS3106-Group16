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
public class TypeNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>TypeNotFoundException</code> without
     * detail message.
     */
    public TypeNotFoundException() {
    }

    /**
     * Constructs an instance of <code>TypeNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TypeNotFoundException(String msg) {
        super(msg);
    }
}
