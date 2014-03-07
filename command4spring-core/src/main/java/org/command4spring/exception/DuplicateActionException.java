package org.command4spring.exception;

public class DuplicateActionException extends DispatchException {
	private static final long serialVersionUID = 1L;

	public DuplicateActionException(final String message) {
		super(message);
	}

}
