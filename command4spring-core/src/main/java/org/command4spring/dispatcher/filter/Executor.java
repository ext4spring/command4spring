package org.command4spring.dispatcher.filter;

import org.command4spring.command.DispatchCommand;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.DispatchResult;

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
