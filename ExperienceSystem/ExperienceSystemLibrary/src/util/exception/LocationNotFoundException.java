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
public class LocationNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>LocationNotFoundException</code> without
     * detail message.
     */
    public LocationNotFoundException() {
    }

    /**
     * Constructs an instance of <code>LocationNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LocationNotFoundException(String msg) {
        super(msg);
    }
}
