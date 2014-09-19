package org.command4spring.result;


/**
 * A common use-case is returning a single value from an action. This provides a simple, type-safe superclass for such
 * results.
 * <p>
 * <b>Note:</b> Subclasses should provide an empty constructor for serialization
 * </p>
 * 
 * @param <T> The value type.
 */
public abstract class AbstractSimpleResult<T> extends AbstractResult implements Result {

    private T value;    
    
    protected AbstractSimpleResult(String commandId) {
        super(commandId);
    }

    public AbstractSimpleResult(String commandId, T value) {
        this(commandId);
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}
