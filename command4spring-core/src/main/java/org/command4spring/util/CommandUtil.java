package org.command4spring.util;

import java.lang.reflect.ParameterizedType;

import org.command4spring.command.Command;
import org.command4spring.result.NoResult;
import org.command4spring.result.Result;

public class CommandUtil {

    @SuppressWarnings("unchecked")
    public static Class<? extends Result> getResultType(final Command<? extends Result> command) {
        return (Class<? extends Result>) ((ParameterizedType) command.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public static boolean isNoResultCommand(final Command<? extends Result> command) {
        Class<? extends Result> resultClass=getResultType(command);
        return NoResult.class.isAssignableFrom(resultClass);
    }

}
