package org.command4spring.dispatcher;

import java.util.Arrays;
import java.util.List;

import org.command4spring.command.AbstractCommand;
import org.command4spring.command.Command;
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
        public <C extends Command<R>, R extends Result> C filter(final C command, final CommandFilterChain filterChain) throws DispatchException {
            if (command instanceof TestCommand) {
                ((TestCommand) command).append("1");
            }
            return filterChain.filter(command);
        }
    }

    private class CommandFilter2 implements CommandFilter {
        @Override
        public <C extends Command<R>, R extends Result> C filter(final C command, final CommandFilterChain filterChain) throws DispatchException {
            if (command instanceof TestCommand) {
                ((TestCommand) command).append("2");
            }
            return filterChain.filter(command);
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
        public <R extends Result> R filter(final R result, final ResultFilterChain filterChain) throws DispatchException {
            if (result instanceof TestResult) {
                ((TestResult) result).append("1");
            }
            return filterChain.filter(result);
        }
    }

    private class ResultFilter2 implements ResultFilter {
        @Override
        public <R extends Result> R filter(final R result, final ResultFilterChain filterChain) throws DispatchException {
            if (result instanceof TestResult) {
                ((TestResult) result).append("2");
            }
            return filterChain.filter(result);
        }
    }

    private class TestDispatcher extends AbstractDispatcher {

        @Override
        protected <C extends Command<R>, R extends Result> R execute(final C command) throws DispatchException {
            TestResult result=new TestResult();
            result.append(((TestCommand)command).getData()+"result");
            return (R)result;
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
