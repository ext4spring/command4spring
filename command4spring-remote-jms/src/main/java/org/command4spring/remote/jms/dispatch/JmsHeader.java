package org.command4spring.remote.jms.dispatch;

public interface JmsHeader {
    public static final String COMMAND_ID_HEADER = "commandId";
    public static final String COMMAND_CLASS_HEADER = "commandClass";
    public static final String RESULT_CLASS="resultClass";
    public static final String RESULT_EXCEPTION_CLASS="resultExceptionClass";
}
