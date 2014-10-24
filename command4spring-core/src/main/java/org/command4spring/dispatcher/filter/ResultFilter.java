package org.command4spring.dispatcher.filter;

import org.command4spring.dispatcher.DispatchResult;
import org.command4spring.exception.DispatchException;

/**
 * Filter interface for dispatchers.
 * @author pborbas
 *
 */
public interface ResultFilter {
    /**
     * Filters a Result. It throws an exception in case of error. The previous filter can handle the error or delegate it.
     * call filterChain.filter to forward the result to the next filter
     * @throws DispatchException
     */
    public DispatchResult filter(DispatchResult dispatchResult, ResultFilterChain filterChain) throws DispatchException;
}
