package org.command4spring.dispatcher;

import java.util.Arrays;
import java.util.List;

import org.command4spring.command.AbstractCommand;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.filter.CommandFilter;
import org.command4spring.dispatcher.filter.CommandFilterChain;
import org.command4spring.dispatcher.filter.DefaultCommandFilterChain;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.LongResult;
import org.command4spring.result.Result;
import org.junit.Assert;
import org.junit.Test;

public class CommandFilterChainTest {

    private class TestCommand extends AbstractCommand<LongResult> {
        String data="";
        void append(final String text) {
            this.data+=text;
        }
        public String getData() {
            return this.data;
        }
    }

    private class CommandFilter1 implements CommandFilter {
	@Override
	public DispatchCommand filter(DispatchCommand dispatchCommand, CommandFilterChain filterChain) throws DispatchException {
            if (dispatchCommand.getCommand() instanceof TestCommand) {
                dispatchCommand.getCommand(TestCommand.class).append("1");
            }
            return filterChain.filter(dispatchCommand);
	}
	
    }

    private class CommandFilter2 implements CommandFilter {
	@Override
	public DispatchCommand filter(DispatchCommand dispatchCommand, CommandFilterChain filterChain) throws DispatchException {
            if (dispatchCommand.getCommand() instanceof TestCommand) {
               dispatchCommand.getCommand(TestCommand.class).append("2");
            }
            return filterChain.filter(dispatchCommand);
	}
    }

    @Test
    public void filterChainCallsFiltersInSpecifiedOrder() throws DispatchException {
        List<CommandFilter> filters=Arrays.asList(new CommandFilter[]{new CommandFilter1(), new CommandFilter2()});
        DefaultCommandFilterChain filterChain=new DefaultCommandFilterChain(filters);
        TestCommand testCommand=new TestCommand();
        DispatchCommand filteredDispatchCommand=filterChain.filter(new DispatchCommand(testCommand));
        TestCommand filteredCommand=filteredDispatchCommand.getCommand(TestCommand.class);
        Assert.assertEquals("12", filteredCommand.getData());
    }

}
