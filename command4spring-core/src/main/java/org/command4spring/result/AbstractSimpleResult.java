package org.command4spring.result;

import java.io.Serializable;

/**
 * A common use-case is returning a single value from an action. This provides a
 * simple, type-safe superclass for such results.
 * <p>
 * <b>Note:</b> Subclasses should provide an empty constructor for
 * serialization
 * </p>
 * 
 * @param <T>
 *            The value type.
 */
public abstract class AbstractSimpleResult<T> extends AbstractResult implements Result, Serializable {

	private static final long serialVersionUID = 1163522627213690452L;
	private T value;

	protected AbstractSimpleResult() {
	}

	public AbstractSimpleResult(T value) {
		this.value = value;
	}

	public T getValue() {
		return this.value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}
