package org.command4spring.dispatcher;

import java.util.Arrays;
import java.util.List;

import org.command4spring.command.AbstractCommand;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.filter.CommandFilter;
import org.command4spring.dispatcher.filter.CommandFilterChain;
import org.command4spring.dispatcher.filter.ResultFilter;
import org.command4spring.dispatcher.filter.ResultFilterChain;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.AbstractResult;
import org.command4spring.result.Result;
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

    private class CommandFilter1 implements CommandFilter {
	@Override
	public DispatchCommand filter(DispatchCommand dispatchCommand, CommandFilterChain filterChain) throws DispatchException {
            Assert.assertEquals(dispatchCommand.getCommand().getCommandId(), dispatchCommand.getHeader(Dispatcher.HEADER_COMMAND_ID));
            Assert.assertEquals(dispatchCommand.getCommand().getClass().getName(), dispatchCommand.getHeader(Dispatcher.HEADER_COMMAND_CLASS));
	    if (dispatchCommand.getCommand() instanceof TestCommand) {
                ((TestCommand) dispatchCommand.getCommand()).append("1");
            }
            return filterChain.filter(dispatchCommand);
	}
	
    }

    private class CommandFilter2 implements CommandFilter {
	@Override
	public DispatchCommand filter(DispatchCommand dispatchCommand, CommandFilterChain filterChain) throws DispatchException {
            if (dispatchCommand.getCommand() instanceof TestCommand) {
                ((TestCommand) dispatchCommand.getCommand()).append("2");
            }
            return filterChain.filter(dispatchCommand);
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

    private class ResultFilter1 implements ResultFilter {
	@Override
	public DispatchResult filter(DispatchResult dispatchResult, ResultFilterChain filterChain) throws DispatchException {
            if (dispatchResult.getResult() instanceof TestResult) {
                ((TestResult) dispatchResult.getResult()).append("1");
            }
            return filterChain.filter(dispatchResult);
	}
    }

    private class ResultFilter2 implements ResultFilter {
	@Override
	public DispatchResult filter(DispatchResult dispatchResult, ResultFilterChain filterChain) throws DispatchException {
            if (dispatchResult.getResult() instanceof TestResult) {
                ((TestResult) dispatchResult.getResult()).append("2");
            }
            return filterChain.filter(dispatchResult);
	}
    }

    private class TestDispatcher extends AbstractDispatcher {
	@Override
	protected DispatchResult execute(DispatchCommand dispatchCommand) throws DispatchException {
            TestResult result=new TestResult();
            result.append(((TestCommand)dispatchCommand.getCommand()).getData()+"result");
            return new DispatchResult(result);
	}
    }

    @Test
    public void filterChainCallsFiltersInSpecifiedOrder() throws DispatchException {
        List<CommandFilter> commandFilters = Arrays.asList(new CommandFilter[] { new CommandFilter1(), new CommandFilter2() });
        List<ResultFilter> resultFilters = Arrays.asList(new ResultFilter[] { new ResultFilter1(), new ResultFilter2() });
        TestDispatcher testDispatcher=new TestDispatcher();
        testDispatcher.setCommandFilters(commandFilters);
        testDispatcher.setResultFilters(resultFilters);
        TestCommand testCommand = new TestCommand();
        TestResult result=testDispatcher.dispatch(testCommand).getResult();
        Assert.assertEquals("12result12", result.getData());
    }

}
