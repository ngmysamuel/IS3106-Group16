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
public class LanguageNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>LanguageNotFoundException</code> without
     * detail message.
     */
    public LanguageNotFoundException() {
    }

    /**
     * Constructs an instance of <code>LanguageNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LanguageNotFoundException(String msg) {
        super(msg);
    }
}
