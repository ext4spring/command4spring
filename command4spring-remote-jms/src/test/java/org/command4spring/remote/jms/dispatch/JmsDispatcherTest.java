package org.command4spring.remote.jms.dispatch;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.jms.Queue;

import org.command4spring.example.SampleCommand;
import org.command4spring.example.SampleResult;
import org.command4spring.exception.AsyncTimeoutException;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.DispatchResult;
import org.command4spring.result.NoResult;
import org.command4spring.result.Result;
import org.command4spring.result.ResultFuture;
import org.command4spring.result.VoidResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-jms-test.xml" })
public class JmsDispatcherTest {

    @Autowired
    JmsDispatcher dispatcher;
    @Autowired
    @Qualifier("CommandDestination")
    Queue commandQueue;
    @Autowired
    @Qualifier("CommandJmsTemplate")
    JmsTemplate JmsTemplate;
    @Autowired
    @Qualifier("ResultDestination")
    Queue resultQueue;

    @Test
    public void testSynchronousJmsExecution() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
        SampleCommand command = new SampleCommand();
        ResultFuture<SampleResult> futureResult = this.dispatcher.dispatch(command);
        SampleResult result = futureResult.getResult();
        Assert.assertEquals(command.getCommandId(), result.getCommandId());
    }

    @Test
    public void testResultContainsRemoteFilterHeaders() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
        SampleCommand command = new SampleCommand();
        ResultFuture<SampleResult> futureResult = this.dispatcher.dispatch(command);
        DispatchResult<? extends Result> result = futureResult.getDispatchResult(5, TimeUnit.SECONDS);
        // Header value is added on the remote side in TestFilter
        Assert.assertEquals(TestFilter.HEADER_VALUE, result.getHeader(TestFilter.HEADER_TEST_FILTER));
    }

    @Test
    public void testAsynchronousJmsExecution() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
        NoResultCommand command = new NoResultCommand();
        ResultFuture<NoResult> futureResult = this.dispatcher.dispatch(command);
        NoResult result = futureResult.getResult();
        Assert.assertEquals(command.getCommandId(), result.getCommandId());
        Thread.sleep(200);
    }

    @Test(expected = TestDispatchException.class)
    public void testExceptionsAreDelegatedBackToJmsDispatcher() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
        TestFailCommand command = new TestFailCommand();
        ResultFuture<VoidResult> futureResult = this.dispatcher.dispatch(command);
        futureResult.getResult();
    }

    @Test(expected = AsyncTimeoutException.class)
    public void testExeptionOnTimeout() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
        TestWaitCommand command = new TestWaitCommand(this.dispatcher.getTimeout() + 500);
        ResultFuture<VoidResult> futureResult = this.dispatcher.dispatch(command);
        futureResult.getResult();
    }

}
