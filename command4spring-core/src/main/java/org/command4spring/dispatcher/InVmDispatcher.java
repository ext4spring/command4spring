package org.command4spring.dispatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.action.Action;
import org.command4spring.command.Command;
import org.command4spring.exception.ActionNotFoundException;
import org.command4spring.exception.DispatchException;
import org.command4spring.exception.DuplicateActionException;
import org.command4spring.result.Result;
import org.command4spring.result.ResultFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of the {@link Dispatcher}
 * 
 * <p>
 * Provides basic action lookup and execution support.
 * </p>
 * 
 * 
 */
public class InVmDispatcher implements Dispatcher {

    private static final Log LOGGER = LogFactory.getLog(InVmDispatcher.class);
    private Map<Class<?>, Action<? extends Command<? extends Result>, ? extends Result>> actionsMap;

    @Override
    @Transactional
    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(final C command) throws DispatchException {
        LOGGER.debug("Execting command:" + command);
        long start = System.currentTimeMillis();
        R result = this.findAction(command).validate(command).execute(command);
        result.setCommandId(command.getCommandId());
        LOGGER.debug("Finished command:" + command + " (" + (System.currentTimeMillis() - start) + "msec)");
        return new ResultFuture<R>(new AsyncResult<R>(result));
    }

    @SuppressWarnings("unchecked")
    protected <C extends Command<R>, R extends Result> Action<C, R> findAction(final C command) throws ActionNotFoundException {
        if (this.actionsMap.containsKey(command.getClass())) {
            return (Action<C, R>) this.actionsMap.get(command.getClass());
        }
        throw new ActionNotFoundException("Action not found for command:" + command);
    }

    @Autowired(required = false)
    public void setActions(final List<Action<? extends Command<? extends Result>, ? extends Result>> actions) throws DuplicateActionException {
        this.actionsMap = new HashMap<Class<?>, Action<? extends Command<? extends Result>, ? extends Result>>();
        for (Action<? extends Command<? extends Result>, ? extends Result> action : actions) {
            if (this.actionsMap.containsKey(action.getCommandType())) {
                throw new DuplicateActionException("Duplicate action:" + action.getClass().getName() + " for command type:" + action.getCommandType());
            }
            LOGGER.info("Action:" + action.getClass().getName() + " registered for command type:" + action.getCommandType());
            this.actionsMap.put(action.getCommandType(), action);
        }
    }

}
