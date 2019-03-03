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
public class CreateNewLanguageException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewLanguageException</code> without
     * detail message.
     */
    public CreateNewLanguageException() {
    }

    /**
     * Constructs an instance of <code>CreateNewLanguageException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewLanguageException(String msg) {
        super(msg);
    }
}
