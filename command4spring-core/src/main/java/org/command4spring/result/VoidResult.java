package org.command4spring.result;

/**
 * Use when your action doesn't have useful result
 * 
 * @author borbasp
 * 
 */
public class VoidResult extends AbstractResult implements Result {

	private static final long serialVersionUID = -4832943487870620012L;

	public VoidResult(String commandId) {
	    super(commandId);
	}
	

}
