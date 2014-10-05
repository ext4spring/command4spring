package org.command4spring.remote.receiver;

import org.command4spring.command.Command;
import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.Result;
import org.command4spring.serializer.Serializer;
//TODO: no need for this for HTTP. deletable?
@Deprecated
public class DefaultCommandReceiver implements CommandReceiver {

    private final Serializer serializer;
    private final Dispatcher dispatcher;

    public DefaultCommandReceiver(final Serializer serializer, final Dispatcher dispatcher) {
        super();
        this.serializer = serializer;
        this.dispatcher = dispatcher;
    }

    @Override
    public String receive(final String textCommand) throws DispatchException {
        Command<? extends Result> command=this.serializer.toCommand(textCommand);
        return this.serializer.toText(this.dispatcher.dispatch(command).getResult());
    }
}
