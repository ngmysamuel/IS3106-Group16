/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author zhangruichun
 */
public class CreateNewExperienceDateException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewExperienceDateException</code>
     * without detail message.
     */
    public CreateNewExperienceDateException() {
    }

    /**
     * Constructs an instance of <code>CreateNewExperienceDateException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewExperienceDateException(String msg) {
        super(msg);
    }
}
