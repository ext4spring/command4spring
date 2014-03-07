package org.command4spring.service;

import org.command4spring.command.Command;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.example.SampleCommand;
import org.command4spring.example.SampleResult;
import org.command4spring.exception.DispatchException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-c4s-core-test.xml"})
public class DispatcherServiceTest {

    @Autowired
    private DispatcherService dispatcherService;
    
    @Test
    public void serviceDelegatesToDispatcher() throws DispatchException {
        SampleCommand command=new SampleCommand();
        SampleResult result=this.dispatcherService.dispatch(command);
        Assert.assertEquals(command.getCommandId(), result.getCommandId());       
    }

    @SuppressWarnings("unchecked")
    @Test(expected=DispatchException.class)
    public void serviceThrowsDispatchErrors() throws DispatchException {
        SampleCommand command=new SampleCommand();
        DefaultDispatcherService mockedDispatcherService=new DefaultDispatcherService();
        Dispatcher mockDispatcher=Mockito.mock(Dispatcher.class);
        Mockito.when(mockDispatcher.dispatch(Mockito.any(Command.class))).thenThrow(new DispatchException("MockException"));
        mockedDispatcherService.setDispatcher(mockDispatcher);
        mockedDispatcherService.dispatch(command);
    }

}
