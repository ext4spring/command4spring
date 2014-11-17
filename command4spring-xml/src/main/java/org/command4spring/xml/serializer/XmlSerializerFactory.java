package org.command4spring.xml.serializer;

import org.command4spring.remote.serializer.Serializer;
import org.command4spring.remote.serializer.SerializerFactory;

public class XmlSerializerFactory implements SerializerFactory {
    @Override
    public boolean isFactoryFor(String serializerName) {
	return "xml".equals(serializerName);
    }

    @Override
    public Serializer create(String serializerName) {
	return new XmlSerializer();
    }
}
