package org.command4spring.result;

/**
 * Use when your action doesn't have useful result but you want to wait the result.
 * 
 * @author borbasp
 * 
 */
public class VoidResult extends AbstractResult implements Result {

    public VoidResult(final String commandId) {
        super(commandId);
    }


}
