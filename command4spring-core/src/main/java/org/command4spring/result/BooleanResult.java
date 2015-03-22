package org.command4spring.result;

public class BooleanResult extends AbstractSimpleResult<Boolean> {

    public BooleanResult(final String commandId, final Boolean value) {
        super(commandId, value);
    }

}
