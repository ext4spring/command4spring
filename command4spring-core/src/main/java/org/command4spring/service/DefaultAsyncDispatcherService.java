package org.command4spring.service;

import java.util.concurrent.Future;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Asynchronous implementation of the {@link DispatcherService}
 */
@Service
public class DefaultAsyncDispatcherService extends AbstractDispatcherService implements AsyncDispatcherService {

	@Override
	@Async
	@Transactional(propagation = Propagation.SUPPORTS)
	public <C extends Command<R>, R extends Result> Future<R> dispatch(C command) throws DispatchException {
		return this.getDispatcher().dispatch(command);
	}
}
