package org.command4spring.service;

import org.command4spring.command.Command;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Synchronous (in VM) implementation of the {@link DispatcherService}
 * 
 */
@Service
public class DefaultDispatcherService extends AbstractDispatcherService implements DispatcherService {

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public <C extends Command<R>, R extends Result> R dispatch(C command) throws DispatchException {
		return this.getDispatcher().dispatch(command);
	}

}
