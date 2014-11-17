package org.command4spring.remote.serializer;

public interface SerializerFactory {
    boolean isFactoryFor(String serializerName);

    Serializer create(String serializerName);
}
