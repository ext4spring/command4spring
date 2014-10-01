package org.command4spring.spring.schedule;

import org.command4spring.command.Command;
import org.command4spring.result.Result;
import org.springframework.scheduling.Trigger;

/**
 * Implementations will be scheduled by CommandScheduler
 * 
 * @author pborbas
 *
 */
public interface ScheduledCommand<C extends Command<R>, R extends Result> {

    /**
     * Create the command which will be executed
     */
    C createCommand();
    
    /**
     * Callback after each execution
     * @param result
     */
    void handleResult(R result);
    
    /**
     * Callback if the action of the command throws an exception
     * @param throwable
     */
    void handleException(Throwable throwable);
    
    /**
     * Creates the trigger which defines the timing of the command execution
     */
    Trigger createTrigger();
    
    
}
