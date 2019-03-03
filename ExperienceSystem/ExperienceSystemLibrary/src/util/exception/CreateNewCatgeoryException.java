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
public class CreateNewCatgeoryException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewCatgeoryException</code> without
     * detail message.
     */
    public CreateNewCatgeoryException() {
    }

    /**
     * Constructs an instance of <code>CreateNewCatgeoryException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewCatgeoryException(String msg) {
        super(msg);
    }
}
