package org.command4spring.dispatcher;

import java.util.Arrays;
import java.util.List;

import org.command4spring.dispatcher.filter.DefaultResultFilterChain;
import org.command4spring.dispatcher.filter.ResultFilter;
import org.command4spring.dispatcher.filter.ResultFilterChain;
import org.command4spring.exception.DispatchException;
import org.command4spring.result.AbstractResult;
import org.junit.Assert;
import org.junit.Test;

public class ResultFilterChainTest {

    private class TestResult extends AbstractResult {
        String data = "";

        void append(final String text) {
            this.data += text;
        }

        public String getData() {
            return this.data;
        }
    }

    private class ResultFilter1 implements ResultFilter {
	@Override
	public DispatchResult filter(DispatchResult dispatchResult, ResultFilterChain filterChain) throws DispatchException {
            if (dispatchResult.getResult() instanceof TestResult) {
                dispatchResult.getResult(TestResult.class).append("1");
            }
            return filterChain.filter(dispatchResult);
	}
    }

    private class ResultFilter2 implements ResultFilter {
	@Override
	public DispatchResult filter(DispatchResult dispatchResult, ResultFilterChain filterChain) throws DispatchException {
            if (dispatchResult.getResult() instanceof TestResult) {
                dispatchResult.getResult(TestResult.class).append("2");
            }
            return filterChain.filter(dispatchResult);
	}
    }


    @Test
    public void filterChainCallsFiltersInSpecifiedOrder() throws DispatchException {
        List<ResultFilter> filters = Arrays.asList(new ResultFilter[] { new ResultFilter1(), new ResultFilter2() });
        DefaultResultFilterChain filterChain = new DefaultResultFilterChain(filters);
        TestResult result = new TestResult();
        DispatchResult dispatchResult=new DispatchResult(result);
        DispatchResult filteredDispatchResult = filterChain.filter(dispatchResult);
        Assert.assertEquals("12", filteredDispatchResult.getResult(TestResult.class).getData());
    }

}
