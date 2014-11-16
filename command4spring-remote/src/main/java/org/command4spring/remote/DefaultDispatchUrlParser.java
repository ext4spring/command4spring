package org.command4spring.remote;

import org.command4spring.remote.exception.DispatchUrlParsingException;
import org.command4spring.remote.model.DispatcherUrl;

public class DefaultDispatchUrlParser implements DispatcherUrlParser {
    /**
     * common format: "protocol://name:serializer@10.0.0.123/path?timeout=5000"
     * protocol specific parameters jms: commandQueue, resultQueue
     */
    private static final String FORMAT = "protocol://name:serializer@host:port/path?timeout=5000&param1=value1&param2=value2";

    @Override
    public DispatcherUrl parse(String urlString) throws DispatchUrlParsingException {
	DispatcherUrl dispatcherUrl = new DispatcherUrl();
	int protocolSeparatorIdx = urlString.indexOf("://");
	if (protocolSeparatorIdx == -1)
	    throw new DispatchUrlParsingException("Invalid URL. Missing protocoll separator '://'. Format: " + FORMAT);
	int atIdx = urlString.indexOf("@");
	if (atIdx == -1)
	    throw new DispatchUrlParsingException("Invalid URL. Missing name and serializer separator '@' Format:" + FORMAT);
	int pathSeparatorIdx = urlString.indexOf("/", atIdx);
	if (pathSeparatorIdx == -1)
	    throw new DispatchUrlParsingException("Invalid URL. Missing path separator '/' after host and port. Format: " + FORMAT);
	int querySeparatorIdx = urlString.indexOf("?", atIdx);
	if (querySeparatorIdx == -1)
	    throw new DispatchUrlParsingException("Invalid URL. Missing query separator '?'. Format: " + FORMAT);
	dispatcherUrl.setProtocol(urlString.substring(0, protocolSeparatorIdx));
	String[] nameAndSerializer = urlString.substring(protocolSeparatorIdx + 3, atIdx).split(":");
	if (nameAndSerializer.length != 2)
	    throw new DispatchUrlParsingException("Invalid URL. Cannot parse name and serializer. Format: " + FORMAT);
	dispatcherUrl.setName(nameAndSerializer[0]);
	dispatcherUrl.setSerializer(nameAndSerializer[1]);
	dispatcherUrl.setHostAndPort(urlString.substring(atIdx + 1, pathSeparatorIdx));
	String query = urlString.substring(querySeparatorIdx + 1);
	for (String queryTuple : query.split("&")) {
	    String[] querySplit = queryTuple.split("=");
	    if (querySplit.length != 2)
		throw new DispatchUrlParsingException("Invalid URL. Cannot parse query part:" + queryTuple + ". Format: " + FORMAT);
	    dispatcherUrl.getParameters().put(querySplit[0], querySplit[1]);
	}
	if (!dispatcherUrl.getParameters().containsKey("timeout")) {
	    throw new DispatchUrlParsingException("Invalid URL. Query parameter 'timeout' is required. Format: " + FORMAT);
	}
	dispatcherUrl.setTimeout(Long.valueOf(dispatcherUrl.getParameters().get("timeout")));
	return dispatcherUrl;
    }

}
