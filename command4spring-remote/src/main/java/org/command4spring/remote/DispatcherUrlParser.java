package org.command4spring.remote;

import org.command4spring.remote.exception.DispatchUrlParsingException;
import org.command4spring.remote.model.DispatcherUrl;

/**
 * Parses an URL into {@link DispatcherUrl} object
 * 
 * @author pborbas
 *
 */
public interface DispatcherUrlParser {
    DispatcherUrl parse(String url) throws DispatchUrlParsingException;
}
