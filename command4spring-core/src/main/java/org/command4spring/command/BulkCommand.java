package org.command4spring.command;

import java.util.LinkedList;
import java.util.List;

import org.command4spring.result.BulkResult;
import org.command4spring.result.Result;
/**
 * Execute multiple command (one by one)
 * Useful if you want to spare some communication overhead.
 * 
 * @author pborbas
 *
 */
//TODO
public class BulkCommand extends AbstractCommand<BulkResult> {

	private static final long serialVersionUID = 1L;

	private final List<Command<? extends Result>> commands = new LinkedList<Command<? extends Result>>();

	public List<? extends Command<? extends Result>> getCommands() {
		return commands;
	}

	public void addCommands(Command<? extends Result> command) {
		this.commands.add(command);
	}

}
