package org.command4spring.testbase.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.command4spring.dispatcher.ChainableDispatcher;
import org.command4spring.example.SampleCommand;
import org.command4spring.example.SampleResult;
import org.command4spring.exception.AsyncTimeoutException;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.DispatchResult;
import org.command4spring.result.NoResult;
import org.command4spring.result.Result;
import org.command4spring.result.ResultCallback;
import org.command4spring.result.ResultFuture;
import org.command4spring.result.VoidResult;
import org.command4spring.testbase.command.NoResultCommand;
import org.command4spring.testbase.command.TestFailCommand;
import org.command4spring.testbase.command.TestWaitCommand;
import org.command4spring.testbase.exception.TestDispatchException;
import org.command4spring.testbase.filter.TestFilter;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public abstract class AbstractDispatcherTest {

    @Test(expected = TestDispatchException.class)
    public void testExceptionsAreDelegatedBackToClientDispatcher() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
        TestFailCommand command = new TestFailCommand();
        ResultFuture<VoidResult> futureResult = this.getDispatcher().dispatch(command);
        futureResult.getResult();
    }
    @Test
    public void testSynchronousExecution() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
        SampleCommand command = new SampleCommand();
        ResultFuture<SampleResult> futureResult = this.getDispatcher().dispatch(command);
        SampleResult result = futureResult.getResult();
        Assert.assertEquals(command.getCommandId(), result.getCommandId());
    }

    @Test
    public void testResultContainsRemoteFilterHeaders() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
        SampleCommand command = new SampleCommand();
        ResultFuture<SampleResult> futureResult = this.getDispatcher().dispatch(command);
        DispatchResult<? extends Result> result = futureResult.getDispatchResult(5, TimeUnit.SECONDS);
        // Header value is added on the remote side in TestFilter
        Assert.assertEquals(TestFilter.HEADER_VALUE, result.getHeader(TestFilter.HEADER_TEST_FILTER));
    }

    @Test
    public void testAsynchronousExecution() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
        NoResultCommand command = new NoResultCommand();
        ResultFuture<NoResult> futureResult = this.getDispatcher().dispatch(command);
        NoResult result = futureResult.getResult();
        Assert.assertEquals(command.getCommandId(), result.getCommandId());
    }

    @Test(expected = AsyncTimeoutException.class)
    public void testExeptionOnTimeout() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
        TestWaitCommand command = new TestWaitCommand(this.getDispatcher().getTimeout() + 500);
        ResultFuture<VoidResult> futureResult = this.getDispatcher().dispatch(command);
        futureResult.getResult();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testResultFutureCallsCallbackOnSuccess() throws DispatchException {
	SampleCommand command = new SampleCommand();
	ResultFuture<SampleResult> futureResult = this.getDispatcher().dispatch(command);
	ResultCallback<SampleResult> resultCallback = Mockito.mock(ResultCallback.class);
	futureResult.registerCallback(resultCallback);
	SampleResult sampleResult = futureResult.getResult();
	Mockito.verify(resultCallback).onSuccess(sampleResult);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testResultFutureCallsCallbackOnFailure() throws DispatchException {
	TestFailCommand command = new TestFailCommand();
	ResultFuture<VoidResult> futureResult = this.getDispatcher().dispatch(command);
	ResultCallback<VoidResult> resultCallback = Mockito.mock(ResultCallback.class);
	futureResult.registerCallback(resultCallback);
	DispatchException dispatchException = null;
	try {
	    futureResult.getResult();
	} catch (DispatchException e) {
	    dispatchException = e;
	}
	Mockito.verify(resultCallback).onError(dispatchException);
    }

    protected abstract ChainableDispatcher getDispatcher();
}
