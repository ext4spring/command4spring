package org.command4spring.dispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.command4spring.action.Action;
import org.command4spring.command.Command;
import org.command4spring.example.SampleAction;
import org.command4spring.example.SampleCommand;
import org.command4spring.example.SampleResult;
import org.command4spring.exception.ActionNotFoundException;
import org.command4spring.exception.CommandValidationException;
import org.command4spring.exception.DispatchException;
import org.command4spring.exception.DuplicateActionException;
import org.command4spring.result.Result;
import org.command4spring.result.ResultFuture;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class InVmDispatcherTest {

    @Test
    public void testDispatcherChooseTheRightAction() throws DispatchException, InterruptedException, ExecutionException {
        InVmDispatcher dispatcher = new InVmDispatcher();
        List<Action<? extends Command<? extends Result>, ? extends Result>> actions = new ArrayList<>();
        actions.add(new SampleAction());
        dispatcher.setActions(actions);
        SampleCommand command = new SampleCommand();
        SampleResult sampleResult = dispatcher.dispatch(command).getResult();
        Assert.assertNotNull(sampleResult);
        Assert.assertEquals(command.getCommandId(), sampleResult.getCommandId());
    }

    @Test
    @Ignore
    public void testThreadedExecutionSpeedtest() throws DispatchException, InterruptedException, ExecutionException {
        ExecutorService executorService;
        executorService=new ForkJoinPool(8);
        long time;
        int count=10000000;
        time=this.measureSpeed(executorService,count);
        System.out.println("throughput/sec:"+(count/(time*1f/1000)));


    }

    private long measureSpeed(final ExecutorService executorService, final int count) throws DispatchException, InterruptedException{
        final InVmDispatcher dispatcher = new InVmDispatcher(executorService);
        List<Action<? extends Command<? extends Result>, ? extends Result>> actions = new ArrayList<>();
        actions.add(new SampleAction());
        dispatcher.setActions(actions);
        final SampleCommand command = new SampleCommand();
        final ArrayBlockingQueue<ResultFuture<SampleResult>> resultQueue=new ArrayBlockingQueue<>(20000);
        long start=System.currentTimeMillis();
        Thread starter=new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<count; i++) {
                    try {
                        resultQueue.offer(dispatcher.dispatch(command), 1, TimeUnit.SECONDS);
                    } catch (DispatchException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        starter.start();
        ResultFuture<SampleResult> sampleResultFuture;
        while ((sampleResultFuture=resultQueue.poll(5, TimeUnit.SECONDS))!=null) {
            sampleResultFuture.getResult();
        }
        return System.currentTimeMillis()-start;

    }


    @Test(expected = ActionNotFoundException.class)
    public void testDispatcherThrowsExceptionIfNoActionFound() throws DispatchException {
        InVmDispatcher dispatcher = new InVmDispatcher();
        List<Action<? extends Command<? extends Result>, ? extends Result>> actions = new ArrayList<>();
        dispatcher.setActions(actions);
        dispatcher.dispatch(new SampleCommand()).getResult();
    }

    @Test(expected = DuplicateActionException.class)
    public void testDispatcherThrowsExceptionIfThereIsMoreActionForACommand() throws DuplicateActionException {
        InVmDispatcher dispatcher = new InVmDispatcher();
        List<Action<? extends Command<? extends Result>, ? extends Result>> actions = new ArrayList<Action<? extends Command<? extends Result>, ? extends Result>>();
        actions.add(new SampleAction());
        actions.add(new SampleAction());
        dispatcher.setActions(actions);
    }


    private class ValidatedAction implements Action<SampleCommand, SampleResult> {

        @Override
        public SampleResult execute(final SampleCommand command) {
            return new SampleResult(command.getCommandId());
        }

        @Override
        public Class<SampleCommand> getCommandType() {
            return SampleCommand.class;
        }

        @Override
        public Action<SampleCommand, SampleResult> validate(final SampleCommand command) throws CommandValidationException {
            if (command.getData()!=null && command.getData().length()>5) {
                throw new CommandValidationException(command, "Invalid SampleCommand.", "Data max size is 5 characters");
            }
            return this;
        }
    }


    @Test(expected = CommandValidationException.class)
    public void dispatcherValidateCommandsBeforeExecution() throws DispatchException {
        InVmDispatcher dispatcher = new InVmDispatcher();
        List<Action<? extends Command<? extends Result>, ? extends Result>> actions = new ArrayList<Action<? extends Command<? extends Result>, ? extends Result>>();
        actions.add(new ValidatedAction());
        dispatcher.setActions(actions);
        SampleCommand invalidCommand=new SampleCommand("123456");
        dispatcher.dispatch(invalidCommand).getResult();
    }
}
