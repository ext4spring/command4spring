package org.command4spring.remote.exception;

import org.command4spring.exception.DispatchException;

public class RemoteDispatchException extends DispatchException {
    private static final long serialVersionUID = 1L;

    public RemoteDispatchException(final String message) {
        super(message);
    }

    public RemoteDispatchException(String message, Exception e) {
        super(message, e);
    }

}
