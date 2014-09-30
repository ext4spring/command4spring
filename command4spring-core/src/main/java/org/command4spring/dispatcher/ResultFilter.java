package org.command4spring.dispatcher;

import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

/**
 * Filter interface for dispatchers.
 * @author pborbas
 *
 */
public interface ResultFilter {
    /**
     * Filters a Result. It throws an exception in case of error. The previous filter can handle the error or delegate it.
     * call filterChain.filter to forward the result to the next filter
     * @param result
     * @return
     * @throws DispatchException
     */
    public <R extends Result> R filter(R result, ResultFilterChain filterChain) throws DispatchException;
}
