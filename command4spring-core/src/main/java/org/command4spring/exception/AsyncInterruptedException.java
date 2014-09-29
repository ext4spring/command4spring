package org.command4spring.exception;
/**
 * Thrown in case of async interrupt
 * @author pborbas
 *
 */
public class AsyncInterruptedException extends AsyncExecutionException {
    private static final long serialVersionUID = 1L;

    public AsyncInterruptedException(final String message, final Exception e) {
        super(message, e);
    }

}
