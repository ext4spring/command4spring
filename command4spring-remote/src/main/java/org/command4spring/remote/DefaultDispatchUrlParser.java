package org.command4spring.remote;

import org.command4spring.remote.exception.DispatchUrlParsingException;
import org.command4spring.remote.model.DispatcherUrl;

public class DefaultDispatchUrlParser implements DispatcherUrlParser {
    /**
     * common format: "protocol://name:serializer@10.0.0.123/path?timeout=5000"
     * protocol specific parameters jms: commandQueue, resultQueue
     */
    @Override
    public DispatcherUrl parse(String urlString) throws DispatchUrlParsingException {
	DispatcherUrl dispatcherUrl = new DispatcherUrl();
	int protocolSeparatorIdx = urlString.indexOf("://");
	if (protocolSeparatorIdx == -1)
	    throw new DispatchUrlParsingException("Invalid URL. Format: protocol://name:serializer@host:port/path?timeout=5000&param1=value1&param2=value2");
	int atIdx = urlString.indexOf("@");
	if (atIdx == -1)
	    throw new DispatchUrlParsingException("Invalid URL. Format: protocol://name:serializer@host:port/path?timeout=5000&param1=value1&param2=value2");
	int pathSeparatorIdx = urlString.indexOf("/", atIdx);
	if (pathSeparatorIdx == -1)
	    throw new DispatchUrlParsingException("Invalid URL. Format: protocol://name:serializer@host:port/path?timeout=5000&param1=value1&param2=value2");
	int querySeparatorIdx = urlString.indexOf("?", atIdx);
	if (querySeparatorIdx == -1)
	    throw new DispatchUrlParsingException("Invalid URL. Format: protocol://name:serializer@host:port/path?timeout=5000&param1=value1&param2=value2");
	dispatcherUrl.setProtocol(urlString.substring(0, protocolSeparatorIdx));
	String[] nameAndSerializer = urlString.substring(protocolSeparatorIdx + 3, atIdx).split(":");
	if (nameAndSerializer.length != 2)
	    throw new DispatchUrlParsingException("Invalid URL. Format: protocol://name:serializer@host:port/path?timeout=5000&param1=value1&param2=value2");
	dispatcherUrl.setName(nameAndSerializer[0]);
	dispatcherUrl.setSerializer(nameAndSerializer[1]);
	dispatcherUrl.setHostAndPort(urlString.substring(atIdx + 1, pathSeparatorIdx));
	String query = urlString.substring(querySeparatorIdx + 1);
	for (String queryTuple : query.split("&")) {
	    String[] querySplit = queryTuple.split("=");
	    if (querySplit.length != 2)
		throw new DispatchUrlParsingException("Invalid URL. Format: protocol://name:serializer@host:port/path?timeout=5000&param1=value1&param2=value2");
	    dispatcherUrl.getParameters().put(querySplit[0], querySplit[1]);
	}
	return dispatcherUrl;
    }

    public static void main(String[] args) throws DispatchUrlParsingException {
	DispatcherUrl dispatcherUrl = new DefaultDispatchUrlParser().parse("protocol://name:serializer@host:port/path?timeout=5000&param1=value1&param2=value2");
	System.out.println(dispatcherUrl);
    }

}
