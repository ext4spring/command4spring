package org.command4spring.result;

/**
 * Use when your action doesn't have useful result like {@link VoidResult} but NoResult also means for async communication protocolls like JMS, to not wait for a result
 * 
 * @author borbasp
 * 
 */
public class NoResult extends AbstractResult implements Result {

    private static final long serialVersionUID = -4832943487870620012L;

    public NoResult(final String commandId) {
        super(commandId);
    }


}
