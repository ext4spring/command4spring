package org.command4spring.jms.service;

import java.util.concurrent.Future;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

/**
 * Sends commands through JMS
 * 
 */
public interface JmsDispatcherService {

	/**
	 * Finds the action for the specified command and executes it. Returns the
	 * appropriate result.
	 * 
	 * @return The action's result.
	 * @throws DispatchException
	 *             if the action execution failed.
	 */
	<C extends Command<R>, R extends Result> Future<R> dispatch(C command) throws DispatchException;
}
