package org.command4spring.spring.dispatcher;

import java.util.List;

import org.command4spring.action.Action;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.dispatcher.InVmDispatcher;
import org.command4spring.dispatcher.filter.DispatchFilter;
import org.command4spring.exception.DispatchException;
import org.command4spring.exception.DuplicateActionException;
import org.command4spring.result.Result;
import org.command4spring.result.ResultFuture;
import org.command4spring.spring.action.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring version of the {@link InVmDispatcher} which contains spring specific annotations for async execution, transactions and autodetect of {@link DispatchAction} classes
 */
public class SpringInVmDispatcher extends InVmDispatcher implements Dispatcher {

    public SpringInVmDispatcher() {
        super();
    }

    @Autowired(required = false)
    public SpringInVmDispatcher(final List<DispatchFilter> filters) {
        super(filters);
    }

    @Override
    @Transactional
    //TODO: transactional won't work with a Dispatcher that uses ExecutorService and runs on separate thread
    public <C extends Command<R>, R extends Result> ResultFuture<R> dispatch(final C command) throws DispatchException {
        return super.dispatch(command);
    }

    @Override
    @Autowired(required = false)
    public void setActions(final List<Action<? extends Command<? extends Result>, ? extends Result>> actions) throws DuplicateActionException {
        super.setActions(actions);
    }

}
