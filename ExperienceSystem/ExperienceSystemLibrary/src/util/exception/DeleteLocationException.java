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
public class DeleteLocationException extends Exception {

    /**
     * Creates a new instance of <code>DeleteLocationException</code> without
     * detail message.
     */
    public DeleteLocationException() {
    }

    /**
     * Constructs an instance of <code>DeleteLocationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteLocationException(String msg) {
        super(msg);
    }
}
