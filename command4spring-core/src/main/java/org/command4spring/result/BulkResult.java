package org.command4spring.result;

import java.util.HashMap;
import java.util.Map;

//TODO: finish bulk exec and error handling
public class BulkResult extends AbstractResult{
	
	private static final long serialVersionUID = 1L;
	private Map<String, Result> results=new HashMap<String, Result>();
	
	@SuppressWarnings("unchecked")
	public <R extends Result> R getResult(String commandId) {
		return (R) results.get(commandId);
	}
	
	public void addResult(String commandId, Result result) {
		this.results.put(commandId, result);
	}
}
