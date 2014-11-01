package org.command4spring.dispatcher;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

/**
 * dispatcher lifecycle callback methods
 * @author pborbas
 *
 */
public interface DispatcherCallback {
    
    void onError(Command<? extends Result> command, DispatchException e) throws DispatchException;

    void onSuccess(Command<? extends Result> command, Result result) throws DispatchException;
}
