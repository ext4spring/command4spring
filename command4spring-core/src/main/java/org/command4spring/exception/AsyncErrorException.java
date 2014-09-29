package org.command4spring.exception;
/**
 * Thrown in case of async interrupt
 * @author pborbas
 *
 */
public class AsyncErrorException extends AsyncExecutionException {
    private static final long serialVersionUID = 1L;

    public AsyncErrorException(final String message, final Exception e) {
        super(message, e);
    }

}
