package org.command4spring.result;

public class LongResult extends AbstractSimpleResult<Long> {

    private static final long serialVersionUID = 1L;

    protected LongResult() {
        // for serialization
    }

    public LongResult(String commandId, Long value) {
        super(commandId, value);
    }

}
