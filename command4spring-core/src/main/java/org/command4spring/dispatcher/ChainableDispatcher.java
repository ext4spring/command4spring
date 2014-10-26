package org.command4spring.dispatcher;

import org.command4spring.command.Command;
import org.command4spring.command.DispatchCommand;
import org.command4spring.dispatcher.callback.DispatcherCallback;
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

    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(DispatchCommand dispatchCommand) throws DispatchException;
    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(DispatchCommand dispatchCommand, final DispatcherCallback callback) throws DispatchException;


}
