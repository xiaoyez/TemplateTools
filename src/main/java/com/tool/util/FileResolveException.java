package com.tool.util;

public class FileResolveException extends Exception {

    public FileResolveException() {
        super();
    }

    public FileResolveException(String message) {
        super(message);
    }

    public FileResolveException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileResolveException(Throwable cause) {
        super(cause);
    }

    protected FileResolveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
