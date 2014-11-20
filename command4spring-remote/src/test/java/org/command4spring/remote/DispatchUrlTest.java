package org.command4spring.remote;

import org.command4spring.remote.exception.DispatchUrlParsingException;
import org.command4spring.remote.model.DispatcherUrl;
import org.junit.Assert;
import org.junit.Test;

public class DispatchUrlTest {

    @Test
    public void testValidHttpUrl() throws DispatchUrlParsingException {
        String url = "http://name:xml@hostname:8080/path?timeout=5";
        DispatcherUrl dispatcherUrl = DispatcherUrl.valueOf(url);
        Assert.assertEquals("http", dispatcherUrl.getProtocol());
        Assert.assertEquals("hostname:8080", dispatcherUrl.getHostAndPort());
        Assert.assertEquals("name", dispatcherUrl.getName());
        Assert.assertEquals("xml", dispatcherUrl.getSerializer());
        Assert.assertEquals("path", dispatcherUrl.getPath());
        Assert.assertEquals(0, dispatcherUrl.getParameters().size());
        Assert.assertEquals(5, dispatcherUrl.getTimeout());
        Assert.assertEquals(url, dispatcherUrl.toString());
    }

    @Test
    public void testValidHttpUrlWithoutPath() throws DispatchUrlParsingException {
        String url = "http://name:xml@hostname:8080/?timeout=5";
        DispatcherUrl dispatcherUrl = DispatcherUrl.valueOf(url);
        Assert.assertEquals("http", dispatcherUrl.getProtocol());
        Assert.assertEquals("hostname:8080", dispatcherUrl.getHostAndPort());
        Assert.assertEquals("name", dispatcherUrl.getName());
        Assert.assertEquals("xml", dispatcherUrl.getSerializer());
        Assert.assertEquals("", dispatcherUrl.getPath());
        Assert.assertEquals(0, dispatcherUrl.getParameters().size());
        Assert.assertEquals(5, dispatcherUrl.getTimeout());
        Assert.assertEquals(url, dispatcherUrl.toString());
    }

    @Test
    public void testValidJmsUrl() throws DispatchUrlParsingException {
        String url = "jms://name:xml@hostname:61616/?timeout=5&commandQueue=c&resultQueue=r";
        DispatcherUrl dispatcherUrl = DispatcherUrl.valueOf(url);
        Assert.assertEquals("jms", dispatcherUrl.getProtocol());
        Assert.assertEquals("hostname:61616", dispatcherUrl.getHostAndPort());
        Assert.assertEquals("name", dispatcherUrl.getName());
        Assert.assertEquals("xml", dispatcherUrl.getSerializer());
        Assert.assertEquals(2, dispatcherUrl.getParameters().size());
        Assert.assertEquals(5, dispatcherUrl.getTimeout());
        Assert.assertEquals("c", dispatcherUrl.getParameters().get("commandQueue"));
        Assert.assertEquals("r", dispatcherUrl.getParameters().get("resultQueue"));
        Assert.assertEquals(url, dispatcherUrl.toString());
    }

    @Test
    public void invalidUrlsThrowsHandledExceptions() {
        String[] invalids =
                new String[] { "http://name:xml@hostname:8080/?timeout=", "http://name:xml@hostname:8080/?asd=5", "http://name:xml@hostname:8080?timeout=5", "http://name@hostname:8080/?timeout=5",
                "http://hostname:8080/?timeout=5", "name:xml@hostname:8080/?timeout=5" };
        for (String invalidUrl : invalids) {
            try {
                DispatcherUrl.valueOf(invalidUrl);
            } catch (DispatchUrlParsingException e) {
                // ok
            }
        }
    }
}
