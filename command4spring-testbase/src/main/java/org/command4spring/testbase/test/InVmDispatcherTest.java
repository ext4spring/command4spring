package org.command4spring.testbase.test;

import org.command4spring.action.Action;
import org.command4spring.command.AbstractCommand;
import org.command4spring.dispatcher.ChainableDispatcher;
import org.command4spring.dispatcher.InVmDispatcher;
import org.command4spring.dispatcher.filter.retry.DefaultRetryPolicy;
import org.command4spring.dispatcher.filter.retry.RetryFilter;
import org.command4spring.exception.CommandValidationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.VoidResult;
import org.command4spring.testbase.dispatcher.TestDispatcherFactory;
import org.junit.Assert;
import org.junit.Test;

public class InVmDispatcherTest extends AbstractDispatcherTest {

    private final InVmDispatcher dispatcher = TestDispatcherFactory.createTestInVmDispatcher();

    @Override
    protected ChainableDispatcher getDispatcher() {
	return this.dispatcher;
    }

    public static class CallCountCommand extends AbstractCommand<VoidResult> {
	final boolean fail;

	public CallCountCommand(boolean fail) {
	    super();
	    this.fail = fail;
	}

    }

    public static class CallCountAction implements Action<CallCountCommand, VoidResult> {
	int count = 0;

	@Override
	public Action<CallCountCommand, VoidResult> validate(CallCountCommand command) throws CommandValidationException {
	    return this;
	}

	@Override
	public VoidResult execute(CallCountCommand command) throws DispatchException {
	    count++;
	    if (command.fail) {
		throw new DispatchException("Failed cause you asked for it");
	    }
	    return new VoidResult(command.getCommandId());
	}

	@Override
	public Class<CallCountCommand> getCommandType() {
	    return CallCountCommand.class;
	}

	public int getCount() {
	    return count;
	}
    }

    @Test
    public void testRetry() throws DispatchException {
	InVmDispatcher inVmDispatcher = new InVmDispatcher();
	inVmDispatcher.setTimeout(30000);
	CallCountAction action = new CallCountAction();
	inVmDispatcher.registerAction(action);
	DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy();
	retryPolicy.setBaseWaitTime(10);
	retryPolicy.setMaxRetryCount(3);
	retryPolicy.setMultiplier(2);
	inVmDispatcher.addFilter(new RetryFilter(retryPolicy));
	try {
	    inVmDispatcher.dispatch(new CallCountCommand(true)).getResult();
	} catch (DispatchException e) {
	    // ok
	}
	Assert.assertEquals(4, action.getCount());
    }

}
