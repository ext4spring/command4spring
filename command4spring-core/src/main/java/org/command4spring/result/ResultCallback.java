package org.command4spring.result;

import org.command4spring.exception.DispatchException;

public interface ResultCallback<T extends Result> {

    void onSuccess(T result);

    void onError(DispatchException dispatchException);

}
