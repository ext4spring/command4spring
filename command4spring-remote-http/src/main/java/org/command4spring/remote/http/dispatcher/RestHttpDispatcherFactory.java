package org.command4spring.remote.http.dispatcher;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.DispatcherFactory;
import org.command4spring.remote.model.DispatcherUrl;
import org.command4spring.remote.serializer.SerializerFactory;

public class RestHttpDispatcherFactory implements DispatcherFactory {

    private final SerializerFactory serializerFactory;

    public RestHttpDispatcherFactory(SerializerFactory serializerFactory) {
	super();
	this.serializerFactory = serializerFactory;
    }

    @Override
    public Dispatcher create(DispatcherUrl url) throws DispatchException {
	if (isFactoryFor(url)) {
	    return new RestHttpDispatcher(this.createTargetUrl(url), this.serializerFactory.create(url.getSerializer()));
	}
	throw new DispatchException("Cannot create HTTP dispatcher for url:" + url);
    }

    protected String createTargetUrl(DispatcherUrl url) {
	return url.getProtocol() + "://" + url.getHostAndPort() + "/" + url.getPath();
    }

    @Override
    public boolean isFactoryFor(DispatcherUrl url) {
	return url.getProtocol().equals("http") || url.getProtocol().equals("https");
    }
}
