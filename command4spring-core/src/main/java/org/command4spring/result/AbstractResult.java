package org.command4spring.result;

public class AbstractResult implements Result {

	private static final long serialVersionUID = 1L;
	private String commandId;

	@Override
	public String getCommandId() {
		return this.commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}
	
	@Override
	public String toString() {
		return "type:"+this.getClass()+" commandId:" + commandId;
	}

}
