package org.command4spring.remote.exception;

import org.command4spring.exception.DispatchException;

/**
 * Parent of all remote exceptions
 * 
 * @author pborbas
 *
 */
public class RemoteDispatchException extends DispatchException {
    private static final long serialVersionUID = 1L;

    public RemoteDispatchException(final String message) {
        super(message);
    }

    public RemoteDispatchException(final String message, final Exception e) {
        super(message, e);
    }

}
