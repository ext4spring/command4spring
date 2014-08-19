package org.command4spring.remote.receiver;

import java.util.concurrent.ExecutionException;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.exception.RemoteDispatchException;
import org.command4spring.serializer.Serializer;

public class DefaultCommandReceiver implements CommandReceiver {

    private final Serializer serializer;
    private final Dispatcher dispatcher;

    public DefaultCommandReceiver(Serializer serializer, Dispatcher dispatcher) {
        super();
        this.serializer = serializer;
        this.dispatcher = dispatcher;
    }

    @Override
    public String receive(String textCommand) throws DispatchException {
        try {
            return this.serializer.toText(this.dispatcher.dispatch(this.serializer.toCommand(textCommand)).get());
        } catch (InterruptedException e) {
            throw new RemoteDispatchException("Error while processing remote command:" + e, e);
        } catch (ExecutionException e) {
            throw new RemoteDispatchException("Error while processing remote command:" + e, e);
        }
    }
}
