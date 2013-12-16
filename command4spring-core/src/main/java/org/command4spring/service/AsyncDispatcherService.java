package org.command4spring.service;

import java.util.concurrent.Future;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

/**
 * Executes actions and returns the results.
 * 
 */
public interface AsyncDispatcherService {

	/**
	 * Finds the action for the specified command and executes it asynchronously. Returns the
	 * appropriate result.
	 * <p>
	 * <b>Note:</b> Spring should be configured to use @Async annotations (annotation driven, task executor). Otherwise it will execute synchronously. 
	 * </p>
	 * 
	 * @return A Future with the action's result.
	 * @throws DispatchException
	 *             if the action execution failed.
	 */
	<C extends Command<R>, R extends Result> Future<R> dispatch(C command) throws DispatchException;
}
