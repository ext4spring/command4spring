package org.command4spring.jms.result;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.command4spring.result.Result;

public class JmsFutureResult<V extends Result> implements Future<V> {

	private final String commandId;
	private final ResultMessageListener resultMessageListener;

	public JmsFutureResult(String commandId, ResultMessageListener resultMessageListener) {
		this.commandId = commandId;
		this.resultMessageListener = resultMessageListener;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		this.resultMessageListener.remove(this.commandId);
		return true;
	}

	@Override
	public boolean isCancelled() {
		return this.resultMessageListener.isHandlerFor(this.commandId);
	}

	@Override
	public boolean isDone() {
		return this.resultMessageListener.hasResultFor(commandId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get() throws InterruptedException, ExecutionException {
		return (V) this.resultMessageListener.waitForResult(commandId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return (V) this.resultMessageListener.waitForResult(commandId, timeout, unit);
	}
}
