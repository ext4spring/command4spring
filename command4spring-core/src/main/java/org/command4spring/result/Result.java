package org.command4spring.result;

import org.command4spring.command.Command;

/**
 * Interface for {@link Command} results.
 * 
 */
public interface Result {
	
	/**
	 * ID of the Command that this Result belogs to. 
	 * @return
	 */
	String getCommandId();
	
	void setCommandId(String commandId);
}
