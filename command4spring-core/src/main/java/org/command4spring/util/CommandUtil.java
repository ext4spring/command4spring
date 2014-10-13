package org.command4spring.util;

import java.lang.reflect.ParameterizedType;

import org.command4spring.command.Command;
import org.command4spring.result.NoResult;
import org.command4spring.result.Result;

public class CommandUtil {

    public static boolean isNoResultCommand(final Command<? extends Result> command) {
        Class<?> resultClass=(Class<?>) ((ParameterizedType) command.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return NoResult.class.isAssignableFrom(resultClass);
    }

}
