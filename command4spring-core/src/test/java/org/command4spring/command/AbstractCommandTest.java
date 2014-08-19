package org.command4spring.command;

import org.command4spring.example.SampleCommand;
import org.command4spring.example.SampleResult;
import org.junit.Assert;
import org.junit.Test;

public class AbstractCommandTest {

    @SuppressWarnings("serial")
    private class TestCommand extends AbstractCommand<SampleResult> {

        public TestCommand(String commandId) {
            super(commandId);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommandMustHaveCommandId() {
        new TestCommand(null);
    }

    @Test
    public void testEqualsUsingCommandId() {
        TestCommand command1a = new TestCommand("1");
        TestCommand command1b = new TestCommand("1");
        TestCommand command2 = new TestCommand("2");
        Assert.assertEquals(command1a, command1a);
        Assert.assertEquals(command1a, command1b);
        Assert.assertNotEquals(command1a, command2);
    }

    @Test
    public void testCommandTypeIsTheImplementationsType() {
        Assert.assertEquals(SampleCommand.class, new SampleCommand().getCommandType());
    }

    @Test
    public void testHashCodeUsingCommandName() {
        SampleCommand sampleCommand = new SampleCommand();
        TestCommand command1a = new TestCommand(sampleCommand.getCommandId());

        TestCommand command1b = new TestCommand("2");
        Assert.assertEquals(command1a.hashCode(), command1b.hashCode());
        Assert.assertNotEquals(command1a.hashCode(), sampleCommand.hashCode());
    }

}
