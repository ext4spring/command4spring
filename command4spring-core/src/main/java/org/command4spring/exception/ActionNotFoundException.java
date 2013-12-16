package org.command4spring.exception;

public class ActionNotFoundException extends DispatchException {
	private static final long serialVersionUID = 1L;

	public ActionNotFoundException(final String message) {
		super(message);
	}

	public ActionNotFoundException(final String message, final Exception e) {
		super(message, e);
	}

}
