package com.agical.bumblebee.parser;

public class CannotFindSourceException extends RuntimeException {
    public CannotFindSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotFindSourceException(String message) {
        super(message);
    }
    private static final long serialVersionUID = 5173322463946315573L;
}
