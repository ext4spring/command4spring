package org.command4spring.remote.receiver;

import org.command4spring.remote.model.TextDispatcherCommand;
import org.command4spring.remote.model.TextDispatcherResult;

public interface CommandReceiver {
    /**
     * Takes the text representation of a command, converts it to an Object
     * command with all the headers, executes the command and converts the
     * Result with all the headers into a text result
     * 
     * @param commandMessage
     * @return
     */
    TextDispatcherResult execute(TextDispatcherCommand commandMessage);
}
