package org.command4spring.dispatcher.callback;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;

public class NopDispatcherCallback implements DispatcherCallback {

    @Override
    public <C extends Command<R>, R extends Result> void beforeDispatch(final C command) throws DispatchException {
    }

    @Override
    public <C extends Command<R>, R extends Result> void onError(final C command, final DispatchException e) throws DispatchException {
    }

    @Override
    public <C extends Command<R>, R extends Result> void onSuccess(final C command, final R result) throws DispatchException {
    }

}
