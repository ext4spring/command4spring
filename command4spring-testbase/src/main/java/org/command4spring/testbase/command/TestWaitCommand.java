package org.command4spring.testbase.command;

import org.command4spring.command.AbstractCommand;
import org.command4spring.result.VoidResult;

public class TestWaitCommand extends AbstractCommand<VoidResult> {
    private static final long serialVersionUID = 1L;
    long waitTime;

    public TestWaitCommand(final long waitTime) {
        super();
        this.waitTime = waitTime;
    }

    public long getWaitTime() {
        return this.waitTime;
    }

}
