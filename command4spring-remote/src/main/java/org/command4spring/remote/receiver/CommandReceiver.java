package org.command4spring.remote.receiver;

import org.command4spring.exception.DispatchException;
import org.command4spring.remote.model.CommandMessage;
import org.command4spring.remote.model.ResultMessage;

public interface CommandReceiver {
    ResultMessage execute(CommandMessage commandMessage) throws DispatchException;
}
