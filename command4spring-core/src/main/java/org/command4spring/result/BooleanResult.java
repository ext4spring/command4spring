package org.command4spring.result;

public class BooleanResult extends AbstractSimpleResult<Boolean> {

	private static final long serialVersionUID = 1L;

	public BooleanResult() {
		this.setValue(false);
	}

	public BooleanResult(Boolean value) {
		super(value);
	}

}
