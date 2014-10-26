package org.command4spring.command;

import java.util.HashMap;
import java.util.Map;

import org.command4spring.result.Result;

/**
 * Wrapps a {@link Command} and adds header feature to it. The
 * {@link CommandFilter}s can use these headers to communicate with each-other
 * (cache info, etc.)
 * 
 * @author pborbas
 *
 */
public class DispatchCommand {
    private final Command<? extends Result> command;
    private final Map<String, String> headers = new HashMap<String, String>();

    public DispatchCommand(Command<? extends Result> command) {
	super();
	this.command = command;
    }

    public Command<? extends Result> getCommand() {
	return command;
    }

    @SuppressWarnings("unchecked")
    public <C extends Command<R>, R extends Result> C getCommand(Class<C> commandType) {
	return (C) command;
    }

    public String getHeader(final String headerName) {
	return this.headers.get(headerName);
    }

    public void setHeader(final String headerName, final String value) {
	this.headers.put(headerName, value);
    }

    public Map<String, String> getHeaders() {
	return this.headers;
    }

}
