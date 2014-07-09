package org.command4spring.exception;

import javax.naming.spi.DirStateFactory.Result;

import org.command4spring.command.Command;

/*
 * Validation exception that contains the error message, the reasons why it failed and the validated command 
 */
public class CommandValidationException extends VadilationException {

    private static final long serialVersionUID = 1L;
    private final Command<? extends Result> command;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public CommandValidationException(Command command, String message, String... reasons) {
        super(message, reasons);
        this.command=command;
    }
    
    public Command<? extends Result> getCommand() {
        return command;
    }

}
