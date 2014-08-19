package org.command4spring.exception;

public class CommandSerializationException extends DispatchException {

    private static final long serialVersionUID = 1L;

    public CommandSerializationException(String message) {
        super(message);
    }

    public CommandSerializationException(String message, Exception e) {
        super(message, e);
    }

}
