package org.command4spring.dispatcher;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;
import org.command4spring.result.ResultFuture;

/**
 * Executes actions and returns the results.
 * 
 */
public interface Dispatcher {
    public static final String HEADER_COMMAND_ID = "commandId";
    public static final String HEADER_COMMAND_CLASS = "commandClass";
    public static final String HEADER_RESULT_CLASS = "resultClass";

    /**
     * Finds the action for the specified command and executes it. Returns the
     * appropriate result.
     * 
     * @return The action's result.
     * @throws DispatchException
     *             if the action execution failed.
     */
    <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(C command) throws DispatchException;
    <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(C command, DispatcherCallback callback) throws DispatchException;

    
}
