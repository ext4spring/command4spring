package org.command4spring.remote;

import org.command4spring.remote.exception.DispatchUrlParsingException;
import org.command4spring.remote.model.DispatcherUrl;

public interface DispatcherUrlParser {
    DispatcherUrl parse(String url) throws DispatchUrlParsingException;
}
