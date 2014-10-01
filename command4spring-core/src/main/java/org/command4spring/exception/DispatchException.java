package org.command4spring.exception;

public class DispatchException extends Exception {
    private static final long serialVersionUID = 1L;

    public DispatchException(final String message) {
        super(message);
    }

    public DispatchException(final String message, final Throwable e) {
        super(message, e);
    }

}
