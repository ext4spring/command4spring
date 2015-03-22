package org.command4spring.remote.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Model of a parsed dispatcher URL
 * 
 * @author pborbas
 *
 */
public class DispatcherUrl {
    private String protocol;
    private String name;
    private String serializer;
    private String hostAndPort;
    private String path;
    private long timeout;
    private Map<String, String> parameters = new HashMap<String, String>();

    public String getPath() {
	return this.path;
    }

    public void setPath(final String path) {
	this.path = path;
    }

    public String getProtocol() {
	return this.protocol;
    }

    public void setProtocol(final String protocol) {
	this.protocol = protocol;
    }

    public String getName() {
	return this.name;
    }

    public void setName(final String name) {
	this.name = name;
    }

    public String getSerializer() {
	return this.serializer;
    }

    public void setSerializer(final String serializer) {
	this.serializer = serializer;
    }

    public String getHostAndPort() {
	return this.hostAndPort;
    }

    public void setHostAndPort(final String hostAndPort) {
	this.hostAndPort = hostAndPort;
    }

    public Map<String, String> getParameters() {
	return this.parameters;
    }

    public void setParameters(final Map<String, String> parameters) {
	this.parameters = parameters;
    }

    public long getTimeout() {
	return this.timeout;
    }

    public void setTimeout(final long timeout) {
	this.timeout = timeout;
    }

    @Override
    public String toString() {
	return "DispatcherUrl [protocol=" + this.protocol + ", name=" + this.name + ", serializer=" + this.serializer + ", timeout=" + this.timeout + ", hostAndPort=" + this.hostAndPort + ", parameters=" + this.parameters + "]";
    }

}
