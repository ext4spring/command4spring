package org.command4spring.result;

import java.io.Serializable;

import org.command4spring.command.Command;

/**
 * Interface for {@link Command} results.
 * 
 */
public interface Result extends Serializable {
	
	/**
	 * ID of the Command that this Result belogs to. 
	 * @return
	 */
	String getCommandId();
	
	void setCommandId(String commandId);
}
