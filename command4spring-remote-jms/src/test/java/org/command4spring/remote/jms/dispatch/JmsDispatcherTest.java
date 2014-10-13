package org.command4spring.remote.jms.dispatch;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.command4spring.example.SampleCommand;
import org.command4spring.example.SampleResult;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.NoResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-jms-test.xml"})
public class JmsDispatcherTest {

    @Autowired
    JmsDispatcher dispatcher;

    @Test
    public void testSynchronousJmsExecution() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
        SampleCommand command=new SampleCommand();
        Future<SampleResult> futureResult=this.dispatcher.dispatch(command);
        SampleResult result=futureResult.get(5, TimeUnit.SECONDS);              
        Assert.assertEquals(command.getCommandId(), result.getCommandId());
    }

    @Test
    public void testAsynchronousJmsExecution() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
        TestCommand command=new TestCommand();
        Future<NoResult> futureResult=this.dispatcher.dispatch(command);
        NoResult result=futureResult.get(5, TimeUnit.SECONDS);              
        Assert.assertEquals(command.getCommandId(), result.getCommandId());
        Thread.sleep(200);
    }

}
