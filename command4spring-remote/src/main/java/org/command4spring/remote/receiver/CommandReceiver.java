package org.command4spring.remote.receiver;

import org.command4spring.exception.DispatchException;

public interface CommandReceiver {

    /**
     * Receive a command's text representation, converts it to object and executes it with a dispatcher. The result is
     * returned as text
     * 
     * @param textCommand
     * @return
     * @throws DispatchException
     */
    String receive(String textCommand) throws DispatchException;

}
