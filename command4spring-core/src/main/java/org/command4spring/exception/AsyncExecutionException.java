package org.command4spring.exception;
/**
 * Thrown in case of async execution throws exception
 * @author pborbas
 *
 */
public class AsyncExecutionException extends DispatchException {
    private static final long serialVersionUID = 1L;

    public AsyncExecutionException(final String message, final Exception e) {
        super(message, e);
    }

}
