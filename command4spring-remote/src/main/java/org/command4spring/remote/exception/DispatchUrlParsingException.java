package org.command4spring.remote.exception;

public class DispatchUrlParsingException extends RemoteDispatchException {
    private static final long serialVersionUID = 1L;

    public DispatchUrlParsingException(final String message) {
	super(message);
    }

    public DispatchUrlParsingException(final String message, final Exception e) {
	super(message, e);
    }

}
