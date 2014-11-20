package org.command4spring.remote.model;

import java.util.HashMap;
import java.util.Map;

import org.command4spring.remote.exception.DispatchUrlParsingException;

public class DispatcherUrl {
    private String protocol;
    private String name;
    private String serializer;
    private String hostAndPort;
    private String path;
    private long timeout;
    private Map<String, String> parameters = new HashMap<String, String>();

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSerializer() {
        return serializer;
    }

    public void setSerializer(final String serializer) {
        this.serializer = serializer;
    }

    public String getHostAndPort() {
        return hostAndPort;
    }

    public void setHostAndPort(final String hostAndPort) {
        this.hostAndPort = hostAndPort;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(final Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(final long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        String urlString=protocol + "://"+ name + ":"+ serializer+"@"+hostAndPort+"/"+path+"?timeout="+timeout;
        for (String paramName:parameters.keySet()) {
            urlString+="&"+paramName+"="+parameters.get(paramName);
        }
        return urlString;
    }

    private static final String FORMAT = "protocol://name:serializer@host:port/path?timeout=5000&param1=value1&param2=value2";

    public static DispatcherUrl valueOf(final String urlString) throws DispatchUrlParsingException {
        DispatcherUrl dispatcherUrl = new DispatcherUrl();
        int protocolSeparatorIdx = urlString.indexOf("://");
        if (protocolSeparatorIdx == -1) {
            throw new DispatchUrlParsingException("Invalid URL. Missing protocoll separator '://'. Format: " + FORMAT);
        }
        int atIdx = urlString.indexOf("@");
        if (atIdx == -1) {
            throw new DispatchUrlParsingException("Invalid URL. Missing name and serializer separator '@' Format:" + FORMAT);
        }
        int pathSeparatorIdx = urlString.indexOf("/", atIdx);
        if (pathSeparatorIdx == -1) {
            throw new DispatchUrlParsingException("Invalid URL. Missing path separator '/' after host and port. Format: " + FORMAT);
        }
        int querySeparatorIdx = urlString.indexOf("?", atIdx);
        if (querySeparatorIdx == -1) {
            throw new DispatchUrlParsingException("Invalid URL. Missing query separator '?'. Format: " + FORMAT);
        }
        dispatcherUrl.setProtocol(urlString.substring(0, protocolSeparatorIdx));
        String[] nameAndSerializer = urlString.substring(protocolSeparatorIdx + 3, atIdx).split(":");
        if (nameAndSerializer.length != 2) {
            throw new DispatchUrlParsingException("Invalid URL. Cannot parse name and serializer. Format: " + FORMAT);
        }
        dispatcherUrl.setName(nameAndSerializer[0]);
        dispatcherUrl.setSerializer(nameAndSerializer[1]);
        dispatcherUrl.setHostAndPort(urlString.substring(atIdx + 1, pathSeparatorIdx));
        dispatcherUrl.setPath(urlString.substring(pathSeparatorIdx + 1, querySeparatorIdx));
        String query = urlString.substring(querySeparatorIdx + 1);
        for (String queryTuple : query.split("&")) {
            String[] querySplit = queryTuple.split("=");
            if (querySplit.length != 2) {
                throw new DispatchUrlParsingException("Invalid URL. Cannot parse query part:" + queryTuple + ". Format: " + FORMAT);
            }
            dispatcherUrl.getParameters().put(querySplit[0], querySplit[1]);
        }
        if (!dispatcherUrl.getParameters().containsKey("timeout")) {
            throw new DispatchUrlParsingException("Invalid URL. Query parameter 'timeout' is required. Format: " + FORMAT);
        }
        dispatcherUrl.setTimeout(Long.valueOf(dispatcherUrl.getParameters().get("timeout")));
        dispatcherUrl.parameters.remove("timeout");
        return dispatcherUrl;
    }
}


