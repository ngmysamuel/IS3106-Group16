/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Asus
 */
public class DeleteExperienceDateException extends Exception {

    /**
     * Creates a new instance of <code>DeleteExperienceDateException</code>
     * without detail message.
     */
    public DeleteExperienceDateException() {
    }

    /**
     * Constructs an instance of <code>DeleteExperienceDateException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteExperienceDateException(String msg) {
        super(msg);
    }
}
