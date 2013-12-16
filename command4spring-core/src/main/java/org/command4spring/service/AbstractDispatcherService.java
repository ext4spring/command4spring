package org.command4spring.service;

import org.command4spring.dispatcher.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDispatcherService {

	@Autowired
	private Dispatcher dispatcher;

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public Dispatcher getDispatcher() {
		return dispatcher;
	}
}
