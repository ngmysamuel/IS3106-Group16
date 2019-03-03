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
public class AppealNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>AppealNotFoundException</code> without
     * detail message.
     */
    public AppealNotFoundException() {
    }

    /**
     * Constructs an instance of <code>AppealNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AppealNotFoundException(String msg) {
        super(msg);
    }
}
