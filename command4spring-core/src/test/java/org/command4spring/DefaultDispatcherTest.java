package org.command4spring;

import java.util.ArrayList;
import java.util.List;

import org.command4spring.action.Action;
import org.command4spring.command.Command;
import org.command4spring.dispatcher.DefaultDispatcher;
import org.command4spring.example.SampleAction;
import org.command4spring.example.SampleCommand;
import org.command4spring.example.SampleResult;
import org.command4spring.exception.ActionNotFoundException;
import org.command4spring.exception.DispatchException;
import org.command4spring.exception.DuplicateActionException;
import org.command4spring.result.Result;
import org.junit.Assert;
import org.junit.Test;

public class DefaultDispatcherTest {

	@Test
	public void testDispatcherChooseTheRightAction() {
		DefaultDispatcher dispatcher = new DefaultDispatcher();
		List<Action<? extends Command<? extends Result>, ? extends Result>> actions = new ArrayList<Action<? extends Command<? extends Result>, ? extends Result>>();
		actions.add(new SampleAction());
		try {
			dispatcher.setActions(actions);
			SampleCommand command=new SampleCommand();
			SampleResult sampleResult = dispatcher.dispatch(command);
			Assert.assertNotNull(sampleResult);
			Assert.assertEquals(command.getCommandId(), sampleResult.getCommandId());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}

	@Test(expected = ActionNotFoundException.class)
	public void testDispatcherThrowsExceptionIfNoActionFound() throws DispatchException {
		DefaultDispatcher dispatcher = new DefaultDispatcher();
		List<Action<? extends Command<? extends Result>, ? extends Result>> actions = new ArrayList<Action<? extends Command<? extends Result>, ? extends Result>>();
		dispatcher.setActions(actions);
		dispatcher.dispatch(new SampleCommand());
	}

	@Test(expected = DuplicateActionException.class)
	public void testDispatcherThrowsExceptionIfThereIsMoreActionForACommand() throws DuplicateActionException {
		DefaultDispatcher dispatcher = new DefaultDispatcher();
		List<Action<? extends Command<? extends Result>, ? extends Result>> actions = new ArrayList<Action<? extends Command<? extends Result>, ? extends Result>>();
		actions.add(new SampleAction());
		actions.add(new SampleAction());
		dispatcher.setActions(actions);
	}

}
