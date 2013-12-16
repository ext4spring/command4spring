package org.command4spring.action;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

/**
 * Instances of this interface will handle specific types of {@link Command}
 * classes.
 * 
 */
public interface Action<C extends Command<R>, R extends Result> {

	/**
	 * Handles the specified action.
	 * 
	 * @return The {@link Result}.
	 * @throws RegistrationException
	 *             if there is a problem performing the specified action.
	 */
	R execute(C command) throws DispatchException;

	/**
	 * @return The type of {@link Command} supported by this action.
	 */
	Class<C> getCommandType();

}
