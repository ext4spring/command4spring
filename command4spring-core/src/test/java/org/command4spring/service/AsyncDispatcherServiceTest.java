package org.command4spring.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.command4spring.example.SampleCommand;
import org.command4spring.example.SampleResult;
import org.command4spring.exception.DispatchException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-c4s-core-test.xml"})
public class AsyncDispatcherServiceTest {

    @Autowired
    private AsyncDispatcherService asyncDispatcherService;
    
    @Test
    public void serviceDelegatesToDispatcher() throws DispatchException, InterruptedException, ExecutionException, TimeoutException {
        SampleCommand command=new SampleCommand();
        Future<SampleResult> futureResult=this.asyncDispatcherService.dispatch(command);
        Assert.assertEquals(command.getCommandId(), futureResult.get(1000, TimeUnit.MILLISECONDS).getCommandId());       
    }

}
