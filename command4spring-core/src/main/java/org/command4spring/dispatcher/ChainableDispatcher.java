package org.command4spring.dispatcher;

import org.command4spring.exception.DispatchException;

/**
 * Adds support to continue another dispatcher's work while keeping header
 * informations
 * 
 * @author pborbas
 *
 */
public interface ChainableDispatcher extends Dispatcher {

    public DispatchResult dispatch(DispatchCommand dispatchCommand) throws DispatchException;


}
