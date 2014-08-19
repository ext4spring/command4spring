package org.command4spring.remote.dispatcher;

import java.util.concurrent.Future;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

/**
 * Executes actions and returns the results.
 * 
 */
public interface RemoteDispatcher {

	/**
	 * Sends the command through the wire and receives the result
	 * 
	 * @return The action's result.
	 * @throws DispatchException
	 *             if the action execution failed.
	 */
	<C extends Command<R>, R extends Result> Future<R> dispatch(C command) throws DispatchException;
}
