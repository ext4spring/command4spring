package org.command4spring.xml.serializer;

import org.command4spring.command.Command;
import org.command4spring.example.SampleCommand;
import org.command4spring.example.SampleResult;
import org.command4spring.exception.CommandSerializationException;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;
import org.junit.Assert;
import org.junit.Test;

public class XmlSerializerTest {

    @Test
    public void testCommandInheritanceSerialization() throws CommandSerializationException {
        SampleCommand sampleCommand = new SampleCommand();
        Serializer serializer = new XmlSerializer();
        String textCommand = serializer.toText(sampleCommand);
        Command<?> command = serializer.toCommand(textCommand);
        Assert.assertEquals(sampleCommand.getClass(), command.getClass());
        Assert.assertTrue(command instanceof SampleCommand);
    }

    @Test
    public void testResultInheritanceSerialization() throws CommandSerializationException {
        SampleResult sampleResult=new SampleResult("1");
        Serializer serializer = new XmlSerializer();
        String textResult = serializer.toText(sampleResult);
        Result result = serializer.toResult(textResult);
        Assert.assertEquals(sampleResult.getClass(), result.getClass());
    }
}
