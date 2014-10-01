package org.command4spring.result;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.command4spring.exception.AsyncErrorException;
import org.command4spring.exception.AsyncInterruptedException;
import org.command4spring.exception.AsyncTimeoutException;
import org.command4spring.exception.DispatchException;
/**
 * Wrapper for Java {@link Future} to force get timeouts and convert Exceptions for more convenient usage
 * @author pborbas
 *
 * @param <T>
 */
public class ResultFuture<T extends Result> implements Future<T> {

    private final Future<T> wrappedFuture;
    //TODO: A configurable max timeout would be fine
    private static final int MAX_TIMEOUT_IN_SECONDS = 300;

    public ResultFuture(final Future<T> wrappedFuture) {
        this.wrappedFuture = wrappedFuture;
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return this.wrappedFuture.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return this.wrappedFuture.isCancelled();
    }

    @Override
    public boolean isDone() {
        return this.wrappedFuture.isDone();
    }

    @Override
    @Deprecated
    /**
     * use getResult instead
     */
    public T get() throws InterruptedException, ExecutionException {
        try {
            return this.wrappedFuture.get(MAX_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new ExecutionException(e);
        }
    }

    @Override
    @Deprecated
    /**
     * use getResult instead
     */
    public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.wrappedFuture.get(timeout, unit);
    }

    public T getResult() throws AsyncTimeoutException, AsyncErrorException, AsyncInterruptedException, DispatchException {
        try {
            return this.wrappedFuture.get(MAX_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new AsyncInterruptedException("Execution interrupted:" + e, e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof DispatchException) {
                throw (DispatchException) e.getCause();
            }
            throw new AsyncErrorException("Execution error:" + e, e);
        } catch (TimeoutException e) {
            throw new AsyncTimeoutException("Execution timed out:" + e, e);
        }
    }

    public T getResult(final long timeout, final TimeUnit unit) throws AsyncTimeoutException, AsyncErrorException, AsyncInterruptedException, DispatchException {
        try {
            return this.wrappedFuture.get(timeout, unit);
        } catch (InterruptedException e) {
            throw new AsyncInterruptedException("Execution interrupted:" + e, e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof DispatchException) {
                throw (DispatchException) e.getCause();
            }
            throw new AsyncErrorException("Execution error:" + e, e);
        } catch (TimeoutException e) {
            throw new AsyncTimeoutException("Execution timed out:" + e, e);
        }
    }

}
