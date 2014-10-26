package org.command4spring.result;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapps a {@link Result} and adds header feature to it. The
 * {@link CommandFilter}s can use these headers to communicate with each-other
 * (cache info, etc.)
 * 
 * @author pborbas
 *
 */
public class DispatchResult<R extends Result> {
    private final R result;
    private final Map<String, String> headers = new HashMap<String, String>();

    public DispatchResult(R result) {
	super();
	this.result = result;
    }

    public Result getResult() {
	return result;
    }
    
    public String getHeader(final String headerName) {
	return this.headers.get(headerName);
    }

    public void setHeader(final String headerName, final String value) {
	this.headers.put(headerName, value);
    }

    public Map<String, String> getHeaders() {
	return this.headers;
    }

}
