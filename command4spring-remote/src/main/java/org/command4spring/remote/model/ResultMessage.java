package org.command4spring.remote.model;

import java.util.HashMap;
import java.util.Map;

import org.command4spring.result.Result;

public class ResultMessage {
    public static final String RESULT_CLASS_HEADER = "resultClass";
    public static final String RESULT_EXCEPTION_CLASS="resultExceptionClass";

    private final Class<? extends Result> resultType;
    private final String textResult;
    private final Map<String, String> header = new HashMap<String, String>();

    public ResultMessage(final String textResult, final Class<? extends Result> resultType) {
        this.textResult=textResult;
        this.resultType=resultType;
    }

    public Class<? extends Result> getResultType() {
        return this.resultType;
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
