package org.command4spring.service;

import java.util.concurrent.ExecutionException;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Synchronous (in VM) implementation of the {@link DispatcherService}
 */
@Service
public class DefaultDispatcherService extends AbstractDispatcherService implements DispatcherService {

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public <C extends Command<R>, R extends Result> R dispatch(C command) throws DispatchException {
        try {
            return this.getDispatcher().dispatch(command).get();
        } catch (InterruptedException e) {
            throw new DispatchException("Error while executing dispatcher:" + e, e);
        } catch (ExecutionException e) {
            throw new DispatchException("Error while executing dispatcher:" + e, e);
        }
    }

}
