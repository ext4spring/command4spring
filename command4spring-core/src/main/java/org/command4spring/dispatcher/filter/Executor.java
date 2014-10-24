package org.command4spring.dispatcher.filter;

import org.command4spring.dispatcher.DispatchCommand;
import org.command4spring.dispatcher.DispatchResult;
import org.command4spring.exception.DispatchException;

/**
 * 
 * @author pborbas
 *
 */
public interface Executor {
    /**
     * Executes the command or delegates it to another dispatcher
     * 
     * @param dispatchCommand
     * @return
     * @throws DispatchException
     */
    public DispatchResult execute(DispatchCommand dispatchCommand) throws DispatchException;

}
