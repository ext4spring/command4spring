package org.command4spring.result;

public class AbstractResult implements Result {

    private String commandId;

    protected AbstractResult() {
        // for serialization
    }
    
    public AbstractResult(final String commandId) {
        if (commandId == null) {
            throw new IllegalArgumentException("Null command ID not allowed");
        }
        this.commandId = commandId;
    }

    @Override
    public String getCommandId() {
        return this.commandId;
    }

    @Override
    public void setCommandId(final String commandId) {
        this.commandId = commandId;
    }

    @Override
    public String toString() {
        return "type:" + this.getClass() + " commandId:" + this.commandId;
    }

}
