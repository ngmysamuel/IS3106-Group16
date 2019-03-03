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
public class DeleteLanguageException extends Exception {

    /**
     * Creates a new instance of <code>DeleteLanguageException</code> without
     * detail message.
     */
    public DeleteLanguageException() {
    }

    /**
     * Constructs an instance of <code>DeleteLanguageException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteLanguageException(String msg) {
        super(msg);
    }
}
