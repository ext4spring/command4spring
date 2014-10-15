package org.command4spring.exception;

import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExceptionUtil {

    private static final Log LOG = LogFactory.getLog(ExceptionUtil.class);

    /**
     * if the class is subclass of {@link DispatchException} and has a String constructor it will return a propert typed
     * class with the message. Otherwise a general {@link DispatchException} is created with the message
     * 
     * @param exceptionClass
     * @param message
     * @return
     */
    public static DispatchException instantiateDispatchException(final String exceptionClass, final String message) {
        try {
            Class<?> clazz = Class.forName(exceptionClass);
            Constructor<?> cons = clazz.getConstructor(String.class);
            Object instance = cons.newInstance(message);
            if (instance instanceof DispatchException) {
                return (DispatchException) instance;
            } else {
                LOG.warn("Unknown exception. Please try to use subclasses of DispatchException for proper error handling.  Class:" + exceptionClass);
                return new DispatchException(message);
            }
        } catch (Throwable e) {
            LOG.warn("Couldn't properly instantiate exception class with String constructor. Class:" + exceptionClass + ", message:" + message + ". Error:" + e);
            return new DispatchException(message);
        }
    }
}