package org.command4spring.remote.jms.dispatch;

import org.command4spring.exception.DispatchException;

public class TestDispatchException extends DispatchException {

    public TestDispatchException(final String message) {
        super(message);
    }


}
