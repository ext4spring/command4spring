package org.command4spring.remote.jms.dispatch;

import org.command4spring.command.DispatchCommand;
import org.command4spring.dispatcher.filter.DispatchFilter;
import org.command4spring.dispatcher.filter.DispatchFilterChain;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.DispatchResult;
import org.command4spring.result.Result;

public class TestFilter implements DispatchFilter {

    public static final String HEADER_TEST_FILTER = "testFilterHeader";
    public static final String HEADER_VALUE = "123124124124124";

    @Override
    public DispatchResult<? extends Result> filter(DispatchCommand dispatchCommand, DispatchFilterChain filterChain) throws DispatchException {
	DispatchResult<? extends Result> dispatchResult = filterChain.filter(dispatchCommand);
	dispatchResult.setHeader(HEADER_TEST_FILTER, HEADER_VALUE);
	return dispatchResult;
    }
}
