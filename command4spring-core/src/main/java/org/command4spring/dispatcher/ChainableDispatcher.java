package org.command4spring.dispatcher;

import org.command4spring.command.Command;
import org.command4spring.command.DispatchCommand;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;
import org.command4spring.result.ResultFuture;

/**
 * Adds support to a {@link Dispatcher} to continue another dispatcher's work
 * while keeping header informations
 * 
 * @author pborbas
 *
 */
public interface ChainableDispatcher extends Dispatcher {

    /**
     * Executes the command with the dispatcher but instead of using a Command it uses a previously prepared {@link DispatchCommand} which contains headers
     * @param dispatchCommand
     * @param requestTimeout
     * @return
     * @throws DispatchException
     */
    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(DispatchCommand dispatchCommand) throws DispatchException;

    public long getTimeout();

}
