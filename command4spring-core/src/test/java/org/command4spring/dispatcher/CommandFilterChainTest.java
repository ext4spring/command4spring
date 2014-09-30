package org.command4spring.dispatcher;

import java.util.Arrays;
import java.util.List;

import org.command4spring.command.AbstractCommand;
import org.command4spring.command.Command;
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

    private class Filter1 implements CommandFilter{
        @Override
        public <C extends Command<R>, R extends Result> C filter(final C command, final CommandFilterChain filterChain) throws DispatchException {
            if (command instanceof TestCommand) {
                ((TestCommand)command).append("1");
            }
            return filterChain.filter(command);
        }
    }
    private class Filter2 implements CommandFilter{
        @Override
        public <C extends Command<R>, R extends Result> C filter(final C command, final CommandFilterChain filterChain) throws DispatchException {
            if (command instanceof TestCommand) {
                ((TestCommand)command).append("2");
            }
            return filterChain.filter(command);
        }
    }

    @Test
    public void filterChainCallsFiltersInSpecifiedOrder() throws DispatchException {
        List<CommandFilter> filters=Arrays.asList(new CommandFilter[]{new Filter1(), new Filter2()});
        DefaultCommandFilterChain filterChain=new DefaultCommandFilterChain(filters);
        TestCommand testCommand=new TestCommand();
        TestCommand filteredCommand=filterChain.filter(testCommand);
        Assert.assertEquals("12", filteredCommand.getData());
    }

}
