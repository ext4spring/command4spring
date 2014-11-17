package org.command4spring.remote;

import org.command4spring.dispatcher.Dispatcher;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.model.DispatcherUrl;

/**
 * Factory that can create a dispatcher based on URL's
 * 
 * @author pborbas
 *
 */
public interface DispatcherFactory {
    /**
     * Tells if this factory can instantiate a {@link Dispatcher} based on the
     * given URL
     */
    boolean isFactoryFor(DispatcherUrl url);

    /**
     * Instantiate a {@link Dispatcher} based on the {@link DispatcherUrl}
     */
    Dispatcher create(DispatcherUrl url) throws DispatchException;
}
