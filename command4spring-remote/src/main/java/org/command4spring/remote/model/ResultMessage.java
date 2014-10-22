package org.command4spring.remote.model;

import java.util.HashMap;
import java.util.Map;

public class ResultMessage {
    public static final String RESULT_EXCEPTION_CLASS="resultExceptionClass";

    private final String textResult;
    private final Map<String, String> header = new HashMap<String, String>();

    public ResultMessage(final String textResult) {
        this.textResult=textResult;
    }

    public String getTextResult() {
        return this.textResult;
    }

    public String getHeader(final String headerName) {
        return this.header.get(headerName);
    }

    public void setHeader(final String headerName, final String value) {
        this.header.put(headerName, value);
    }

}
