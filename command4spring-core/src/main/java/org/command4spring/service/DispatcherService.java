package org.command4spring.service;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

/**
 * Executes actions and returns the results.
 * 
 */
public interface DispatcherService {

	/**
	 * Finds the action for the specified command and executes it. Returns the
	 * appropriate result.
	 * 
	 * @return The action's result.
	 * @throws DispatchException
	 *             if the action execution failed.
	 */
	<C extends Command<R>, R extends Result> R dispatch(C command) throws DispatchException;
}
