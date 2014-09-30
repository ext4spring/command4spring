package org.command4spring.dispatcher;

import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

/**
 * Filter interface for dispatchers.
 * @author pborbas
 *
 */
public interface ResultFilterChain {
    /**
     * Calls the next filter in the chain
     * @param result
     * @return
     * @throws DispatchException
     */
    public <R extends Result> R filter(R result) throws DispatchException;
}
