package org.command4spring.action;

import java.lang.reflect.ParameterizedType;

import org.command4spring.command.Command;
import org.command4spring.exception.CommandValidationException;
import org.command4spring.result.Result;

public abstract class AbstractAction<C extends Command<R>, R extends Result> implements Action<C, R> {

    @SuppressWarnings("unchecked")
    @Override
    public Class<C> getCommandType() {
        return (Class<C>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public Action<C, R> validate(final C listCustomersCommand) throws CommandValidationException {
        return this;
    }

}
