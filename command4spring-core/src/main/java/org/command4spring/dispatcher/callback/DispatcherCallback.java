package org.command4spring.dispatcher.callback;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

/**
 * dispatcher lifecycle callback methods
 * @author pborbas
 *
 */
public interface DispatcherCallback {
    <C extends Command<R>, R extends Result> void beforeDispatch(C command) throws DispatchException;
    
    <C extends Command<R>, R extends Result> void onError(C command, DispatchException e) throws DispatchException;

    <C extends Command<R>, R extends Result> void onSuccess(C command, R result) throws DispatchException;
}
