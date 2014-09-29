package org.command4spring.exception;
/**
 * Thrown in case of async timeout
 *
 * @author pborbas
 *
 */
public class AsyncTimeoutException extends AsyncExecutionException {
    private static final long serialVersionUID = 1L;

    public AsyncTimeoutException(final String message, final Exception e) {
        super(message, e);
    }

}
