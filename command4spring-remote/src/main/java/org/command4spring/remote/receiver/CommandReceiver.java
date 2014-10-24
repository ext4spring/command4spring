package org.command4spring.remote.receiver;

import org.command4spring.exception.DispatchException;
import org.command4spring.remote.model.TextDispatcherCommand;
import org.command4spring.remote.model.TextDispatcherResult;

public interface CommandReceiver {
    TextDispatcherResult execute(TextDispatcherCommand commandMessage) throws DispatchException;
}
