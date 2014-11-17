package org.command4spring.spring.dispatcher;

import java.util.List;

import org.command4spring.exception.DispatchException;
import org.command4spring.remote.DispatcherFactory;
import org.command4spring.remote.model.DispatcherUrl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Holds all dispatcher factories in the spring context (@Autowired)
 * 
 * @author pborbas
 *
 */
public class SpringDispatcherFactoryHolder {

    List<DispatcherFactory> factories;

    @Autowired
    public SpringDispatcherFactoryHolder(List<DispatcherFactory> factories) {
	super();
	this.factories = factories;
    }
    
    /**
     * returns the right {@link DispatcherFactory} for the given
     * {@link DispatcherUrl} or throws exception if not found.
     */
    public DispatcherFactory getFactory(DispatcherUrl url) throws DispatchException {
	for (DispatcherFactory factory : this.factories) {
	    if (factory.isFactoryFor(url)) {
		return factory;
	    }
	}
	throw new DispatchException("Cannot find dispatcher factory for URL:" + url);
    }
    
    
}
