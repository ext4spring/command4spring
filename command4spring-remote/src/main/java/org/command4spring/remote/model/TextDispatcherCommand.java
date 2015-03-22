package org.command4spring.remote.model;

import java.util.HashMap;
import java.util.Map;

import org.command4spring.command.DispatchCommand;

/**
 * Serialized version of a {@link DispatchCommand}
 * 
 * @author pborbas
 *
 */
public class TextDispatcherCommand {

    private final String textCommand;
    private final Map<String, String> headers = new HashMap<String, String>();

    public TextDispatcherCommand(final String textCommand) {
	this.textCommand = textCommand;
    }

    public String getTextCommand() {
	return this.textCommand;
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
