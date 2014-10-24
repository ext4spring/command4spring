package org.command4spring.remote.dispatcher;

import org.command4spring.dispatcher.Dispatcher;

/**
 * Executes actions and returns the results.
 * 
 */
public interface RemoteDispatcher extends Dispatcher {
    public static final String HEADER_RESULT_EXCEPTION_CLASS="resultExceptionClass";

}
