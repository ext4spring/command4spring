package org.command4spring.jms.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.command4spring.example.SampleCommand;
import org.command4spring.example.SampleResult;
import org.command4spring.exception.DispatchException;
import org.command4spring.jms.result.JmsEmptyFutureResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-jms-without-response-test.xml"})
public class JmsDispatcherServiceWithoutResultTest {

	@Autowired
	JmsDispatcherService jmsDispatcherService;
	
	@Test
	public void testSynchronousJmsExecution() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
		SampleCommand command=new SampleCommand();
		Future<SampleResult> futureResult=this.jmsDispatcherService.dispatch(command);
		Assert.assertTrue(futureResult instanceof JmsEmptyFutureResult);
	}
	
}
