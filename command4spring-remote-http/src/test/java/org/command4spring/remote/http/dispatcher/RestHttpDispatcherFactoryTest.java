package org.command4spring.remote.http.dispatcher;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.example.SampleCommand;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.exception.RemoteDispatchException;
import org.command4spring.remote.model.DispatcherUrl;
import org.command4spring.remote.serializer.SerializerFactory;
import org.command4spring.xml.serializer.XmlSerializerFactory;
import org.junit.Assert;
import org.junit.Test;

public class RestHttpDispatcherFactoryTest {

    @Test
    public void testCreateDispatcherByUrl() throws DispatchException {
        SerializerFactory serializerFactory=new XmlSerializerFactory();
        RestHttpDispatcherFactory dispatcherFactory=new RestHttpDispatcherFactory(serializerFactory);
        DispatcherUrl dispatcherUrl=DispatcherUrl.valueOf("http://restdispatcher:xml@127.0.0.1:9999/dispatcher?timeout=5000");
        Assert.assertTrue(dispatcherFactory.isFactoryFor(dispatcherUrl));
        Dispatcher dispatcher=dispatcherFactory.create(dispatcherUrl);
        Assert.assertTrue(dispatcher instanceof RestHttpDispatcher);
        Assert.assertEquals(5000, ((RestHttpDispatcher)dispatcher).getTimeout());
        try {
            dispatcher.dispatch(new SampleCommand()).getResult();
        } catch (RemoteDispatchException e) {
            Assert.assertTrue(e.getMessage().contains("http://127.0.0.1:9999"));
        }
    }
}
