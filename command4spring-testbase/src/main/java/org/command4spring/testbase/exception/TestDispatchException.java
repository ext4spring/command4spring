package org.command4spring.testbase.exception;

import org.command4spring.exception.DispatchException;

public class TestDispatchException extends DispatchException {
    private static final long serialVersionUID = 1L;

    public TestDispatchException(final String message) {
        super(message);
    }


}
