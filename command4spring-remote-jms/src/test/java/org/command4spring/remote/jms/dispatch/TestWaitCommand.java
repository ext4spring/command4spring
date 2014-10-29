package org.command4spring.remote.jms.dispatch;

import org.command4spring.command.AbstractCommand;
import org.command4spring.result.VoidResult;

public class TestWaitCommand extends AbstractCommand<VoidResult> {
    long waitTime;

    public TestWaitCommand(final long waitTime) {
        super();
        this.waitTime = waitTime;
    }

    public long getWaitTime() {
        return this.waitTime;
    }

}
