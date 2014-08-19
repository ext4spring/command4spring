package org.command4spring.command;

import java.io.Serializable;

import org.command4spring.result.Result;
import org.command4spring.service.DispatcherService;

/**
 * Represents a command sent to the {@link DispatcherService}. It has a specific
 * result type which is returned if the action is successful.
 * 
 * @param <R>
 *            The {@link Result} type.
 */
public interface Command<R extends Result> extends Serializable {
    
	@Override
	boolean equals(Object o);

	@Override
	int hashCode();

        Class<?> getCommandType();

        String getCommandId();
}
