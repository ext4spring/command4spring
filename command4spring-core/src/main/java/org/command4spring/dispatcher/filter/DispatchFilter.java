package org.command4spring.dispatcher.filter;

import org.command4spring.dispatcher.DispatchCommand;
import org.command4spring.dispatcher.DispatchResult;
import org.command4spring.exception.DispatchException;

/**
 * Filter interface for dispatchers.
 * 
 * @author pborbas
 *
 */
public interface DispatchFilter {
    /**
     * Filters a Command. It throws an exception in case of error. The previous
     * filter can handle the error or delegate it. call filterChain.filter to
     * forward the command to the next filter. Each filter can return a result
     * (cache). The last should execute the command of forward to another
     * dispatcher (remoting)
     * 
     * @param command
     * @return
     * @throws DispatchException
     */
    public DispatchResult filter(DispatchCommand dispatchCommand, DispatchFilterChain filterChain) throws DispatchException;

}
