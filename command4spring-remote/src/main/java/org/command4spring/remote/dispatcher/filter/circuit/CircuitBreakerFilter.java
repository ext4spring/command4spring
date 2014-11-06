package org.command4spring.remote.dispatcher.filter.circuit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.command4spring.command.DispatchCommand;
import org.command4spring.dispatcher.filter.DispatchFilter;
import org.command4spring.dispatcher.filter.DispatchFilterChain;
import org.command4spring.exception.DispatchException;
import org.command4spring.remote.exception.RemoteDispatchException;
import org.command4spring.result.DispatchResult;
import org.command4spring.result.Result;
/**
 * Circuite breaker implementation
 * @author pborbas
 *
 */
public class CircuitBreakerFilter implements DispatchFilter {

    private final Log LOGGER = LogFactory.getLog(CircuitBreakerFilter.class);

    private CircuitBreakerState state = CircuitBreakerState.CLOSED;
    private long stateChangeTime = System.currentTimeMillis();
    private int errorCount = 0;
    private int successCount = 0;
    private int errorThreshold = 5;
    private int successThreshold = 1;
    private long openInterval = 15000;
    private long halfOpenIntervall = 5000;

    @Override
    public DispatchResult<? extends Result> filter(final DispatchCommand dispatchCommand, final DispatchFilterChain filterChain) throws DispatchException {
        if (this.executionAllowed()) {
            try {
                final DispatchResult<? extends Result> result = filterChain.filter(dispatchCommand);
                this.bookSuccess();
                return result;
            } catch (RemoteDispatchException e) {
                this.bookError();
                throw e;
            } catch (DispatchException e) {
                this.bookSuccess(); //this counts as success
                throw e;
            }
        }
        throw new CircuiteBreakerException("Circuite breaker refused this call");
    }

    protected boolean executionAllowed() {
        if (this.state == CircuitBreakerState.CLOSED) {
            return true;
        } else if (this.state == CircuitBreakerState.HALF_OPEN) {
            if (System.currentTimeMillis() > this.stateChangeTime + this.halfOpenIntervall) {
                this.changeState(CircuitBreakerState.HALF_OPEN);
                this.LOGGER.debug("Allowing call in " + CircuitBreakerState.HALF_OPEN + " state");
                return true;
            }
            return false;
        } else {
            if (System.currentTimeMillis() > this.stateChangeTime + this.openInterval) {
                this.changeState(CircuitBreakerState.HALF_OPEN);
                this.LOGGER.debug("Switched to " + CircuitBreakerState.HALF_OPEN + " state");
                return true;
            }
            return false;
        }
    }

    protected void bookSuccess() {
        if (this.state != CircuitBreakerState.CLOSED) {
            this.successCount++;
            if (this.successCount >= this.successThreshold) {
                this.state = CircuitBreakerState.CLOSED;
                this.errorCount = 0;
            }
        }
    }

    protected void bookError() {
        if (this.state == CircuitBreakerState.CLOSED) {
            this.errorCount++;
            if (this.errorCount >= this.errorThreshold) {
                this.changeState(CircuitBreakerState.OPEN);
                this.successCount = 0;
            }
        }
    }

    protected void changeState(final CircuitBreakerState state) {
        this.stateChangeTime = System.currentTimeMillis();
        this.state = state;
        if (state==CircuitBreakerState.OPEN) {
            this.LOGGER.warn("State changed to "+this.state);
        } else {
            this.LOGGER.info("State changed to "+this.state);
        }
    }

    public CircuitBreakerState getState() {
        return this.state;
    }

    public int getErrorThreshold() {
        return this.errorThreshold;
    }

    public int getSuccessThreshold() {
        return this.successThreshold;
    }

    public long getWaitAfterOpen() {
        return this.openInterval;
    }

    public long getHalfOpenIntervall() {
        return this.halfOpenIntervall;
    }

    /**
     * After this number of error the cirtcuite goes to OPEN
     * @param errorThreshold
     */
    public void setErrorThreshold(final int errorThreshold) {
        this.errorThreshold = errorThreshold;
    }

    /**
     * After this number of succes in HALF_OPEN state the cirtuit goes back to CLOSED
     * @param successThreshold
     */
    public void setSuccessThreshold(final int successThreshold) {
        this.successThreshold = successThreshold;
    }

    /**
     * After this number of milliseconds in OPEN state the circuit goes to HALF_OPEN 
     * @param openInterval
     */
    public void setOpenInterval(final long openInterval) {
        this.openInterval = openInterval;
    }

    /**
     * In HALF_OPEN mode the circuit let a request through in every halfOpenInterval milliseconds
     * @param halfOpenIntervall
     */
    public void setHalfOpenIntervall(final long halfOpenIntervall) {
        this.halfOpenIntervall = halfOpenIntervall;
    }


}
