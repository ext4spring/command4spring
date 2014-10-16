package org.command4spring.remote.model;

import java.util.HashMap;
import java.util.Map;

import org.command4spring.command.Command;
import org.command4spring.result.Result;
import org.command4spring.util.CommandUtil;

public class CommandMessage {
    public static final String COMMAND_ID_HEADER = "commandId";
    public static final String COMMAND_CLASS_HEADER = "commandClass";

    private final Command<? extends Result> command;
    private final String textCommand;
    private final Map<String, String> headers = new HashMap<String, String>();

    public CommandMessage(final Command<? extends Result> command, final String textCommand) {
        this.command = command;
        this.textCommand = textCommand;
    }

    public Command<? extends Result> getCommand() {
        return this.command;
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

    public Class<? extends Result> getResultType() {
        return CommandUtil.getResultType(this.command);
    }

    public boolean isNoResultCommand() {
        return CommandUtil.isNoResultCommand(this.command);
    }
}
