package org.command4spring.remote.dispatcher.filter.circuit;

import org.command4spring.remote.exception.RemoteDispatchException;

@SuppressWarnings("serial")
public class CircuiteBreakerException extends RemoteDispatchException {

    public CircuiteBreakerException(final String message) {
        super(message);
    }

    public CircuiteBreakerException(final String message, final Exception e) {
        super(message, e);
    }

}
