package com.michael.reflect.exception;

/**
 * FormException
 */
public class FormException extends Exception {
    private final static long serialVersionUID = 1L;

    public FormException(String message) {
        super("FormException: "+message);
    }
    
}