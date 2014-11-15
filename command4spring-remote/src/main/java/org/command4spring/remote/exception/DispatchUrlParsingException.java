package org.command4spring.remote.exception;

public class DispatchUrlParsingException extends RemoteDispatchException {

    public DispatchUrlParsingException(String message) {
	super(message);
    }

    public DispatchUrlParsingException(String message, Exception e) {
	super(message, e);
    }

}
