package org.command4spring.dispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.command4spring.command.AbstractCommand;
import org.command4spring.command.DispatchCommand;
import org.command4spring.dispatcher.filter.DispatchFilter;
import org.command4spring.dispatcher.filter.DispatchFilterChain;
import org.command4spring.dispatcher.filter.Executor;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.AbstractResult;
import org.command4spring.result.DispatchResult;
import org.junit.Assert;
import org.junit.Test;

public class AbstractDispatcherTest {

    private class TestCommand extends AbstractCommand<TestResult> {
	String data = "";

	void append(final String text) {
	    this.data += text;
	}

	public String getData() {
	    return this.data;
	}
    }

    private class DispatchFilter1 implements DispatchFilter {
	@Override
	public DispatchResult filter(DispatchCommand dispatchCommand, DispatchFilterChain filterChain) throws DispatchException {
	    Assert.assertEquals(dispatchCommand.getCommand().getCommandId(), dispatchCommand.getHeader(Dispatcher.HEADER_COMMAND_ID));
	    Assert.assertEquals(dispatchCommand.getCommand().getClass().getName(), dispatchCommand.getHeader(Dispatcher.HEADER_COMMAND_CLASS));
	    if (dispatchCommand.getCommand() instanceof TestCommand) {
		((TestCommand) dispatchCommand.getCommand()).append("1");
	    }
	    DispatchResult dispatchResult = filterChain.filter(dispatchCommand);
	    if (dispatchResult.getResult() instanceof TestResult) {
		((TestResult) dispatchResult.getResult()).append("1");
	    }
	    return dispatchResult;
	}
    }

    private class DispatchFilter2 implements DispatchFilter {
	@Override
	public DispatchResult filter(DispatchCommand dispatchCommand, DispatchFilterChain filterChain) throws DispatchException {
	    if (dispatchCommand.getCommand() instanceof TestCommand) {
		((TestCommand) dispatchCommand.getCommand()).append("2");
	    }
	    DispatchResult dispatchResult = filterChain.filter(dispatchCommand);
	    if (dispatchResult.getResult() instanceof TestResult) {
		((TestResult) dispatchResult.getResult()).append("2");
	    }
	    return dispatchResult;
	}
    }

    private class TestResult extends AbstractResult {
	String data = "";

	void append(final String text) {
	    this.data += text;
	}

	public String getData() {
	    return this.data;
	}
    }

    private class TestDispatcher extends AbstractDispatcher implements Executor {

	public TestDispatcher(List<DispatchFilter> filters) {
	    super(filters);
	}

	@Override
	public DispatchResult execute(DispatchCommand dispatchCommand) throws DispatchException {
	    Assert.assertEquals(dispatchCommand.getCommand().getCommandId(), dispatchCommand.getHeader(Dispatcher.HEADER_COMMAND_ID));
	    Assert.assertEquals(dispatchCommand.getCommand().getCommandType().getName(), dispatchCommand.getHeader(Dispatcher.HEADER_COMMAND_CLASS));
	    TestResult result = new TestResult();
	    result.append(((TestCommand) dispatchCommand.getCommand()).getData() + "result");
	    return new DispatchResult(result);
	}

	@Override
	protected Executor getExecutor() {
	    return this;
	}
    }

    @Test
    public void filterChainCallsFiltersInSpecifiedOrder() throws DispatchException {
	List<DispatchFilter> filters = Arrays.asList(new DispatchFilter[] { new DispatchFilter1(), new DispatchFilter2() });
	TestDispatcher testDispatcher = new TestDispatcher(filters);
	TestCommand testCommand = new TestCommand();
	TestResult result = testDispatcher.dispatch(testCommand).getResult();
	Assert.assertEquals("12result21", result.getData());
    }

    @Test
    public void resultFutureAlsoContainsDispatchResultWithCorrectResultAndDefaultHeaders() throws DispatchException {
	TestDispatcher testDispatcher = new TestDispatcher(new ArrayList<DispatchFilter>());
	TestCommand testCommand = new TestCommand();
	DispatchResult<TestResult> dispatchResult = testDispatcher.dispatch(testCommand).getDispatchResult(5, TimeUnit.MINUTES);
	Assert.assertNotNull(dispatchResult);
	Assert.assertNotNull(dispatchResult.getResult());
	Assert.assertTrue(dispatchResult.getResult() instanceof TestResult);
	Assert.assertEquals(testCommand.getCommandId(), dispatchResult.getResult().getCommandId());
	Assert.assertEquals(testCommand.getCommandId(), dispatchResult.getHeader(Dispatcher.HEADER_COMMAND_ID));
	Assert.assertEquals(TestResult.class.getName(), dispatchResult.getHeader(Dispatcher.HEADER_RESULT_CLASS));
    }

}
