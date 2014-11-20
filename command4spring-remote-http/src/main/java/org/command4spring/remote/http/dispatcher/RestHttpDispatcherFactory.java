package org.command4spring.remote.http.dispatcher;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.DispatcherFactory;
import org.command4spring.remote.model.DispatcherUrl;
import org.command4spring.remote.serializer.SerializerFactory;

public class RestHttpDispatcherFactory implements DispatcherFactory {

    private final SerializerFactory serializerFactory;

    public RestHttpDispatcherFactory(final SerializerFactory serializerFactory) {
        super();
        this.serializerFactory = serializerFactory;
    }

    @Override
    public Dispatcher create(final DispatcherUrl url) throws DispatchException {
        if (isFactoryFor(url)) {
            RestHttpDispatcher dispatcher = new RestHttpDispatcher(createTargetUrl(url), serializerFactory.create(url.getSerializer()));
            dispatcher.setTimeout(url.getTimeout());
            return dispatcher;
        }
        throw new DispatchException("Cannot create HTTP dispatcher for url:" + url);
    }

    protected String createTargetUrl(final DispatcherUrl url) {
        String targetUrl = url.getProtocol() + "://" + url.getHostAndPort();
        if (url.getPath() != null && url.getPath().length() > 0) {
            targetUrl += "/" + url.getPath();
        }
        return targetUrl;
    }

    @Override
    public boolean isFactoryFor(final DispatcherUrl url) {
        return url.getProtocol().equals("http") || url.getProtocol().equals("https");
    }
}
