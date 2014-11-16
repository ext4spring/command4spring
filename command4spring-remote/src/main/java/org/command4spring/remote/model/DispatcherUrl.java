package org.command4spring.remote.model;

import java.util.HashMap;
import java.util.Map;

public class DispatcherUrl {
    private String protocol;
    private String name;
    private String serializer;
    private String hostAndPort;
    private long timeout;
    private Map<String, String> parameters = new HashMap<String, String>();

    public String getProtocol() {
	return protocol;
    }

    public void setProtocol(String protocol) {
	this.protocol = protocol;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getSerializer() {
	return serializer;
    }

    public void setSerializer(String serializer) {
	this.serializer = serializer;
    }

    public String getHostAndPort() {
	return hostAndPort;
    }

    public void setHostAndPort(String hostAndPort) {
	this.hostAndPort = hostAndPort;
    }

    public Map<String, String> getParameters() {
	return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
	this.parameters = parameters;
    }

    public long getTimeout() {
	return timeout;
    }

    public void setTimeout(long timeout) {
	this.timeout = timeout;
    }

    @Override
    public String toString() {
	return "DispatcherUrl [protocol=" + protocol + ", name=" + name + ", serializer=" + serializer + ", timeout=" + timeout + ", hostAndPort=" + hostAndPort + ", parameters=" + parameters + "]";
    }
    
}
