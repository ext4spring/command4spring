package org.command4spring.command;

import java.util.UUID;

import org.command4spring.result.Result;

public class AbstractCommand<R extends Result> implements Command<R> {

	private static final long serialVersionUID = 1L;

	private final String commandId;

	public AbstractCommand() {
		this.commandId = UUID.randomUUID().toString();
	}

	public AbstractCommand(String commandId) {
		if (commandId == null) {
			throw new IllegalArgumentException("Null command ID not allowed");
		}
		this.commandId = commandId;
	}

	@Override
	public String getCommandId() {
		return this.commandId;
	}

	@Override
	public int hashCode() {
		return this.getClass().getName().hashCode();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		return (obj!=null && obj instanceof AbstractCommand && ((AbstractCommand)obj).getCommandId().equals(this.commandId));
	}

	@Override
	public String toString() {
		return "type:"+this.getClass()+" commandId:" + commandId;
	}

	
}
