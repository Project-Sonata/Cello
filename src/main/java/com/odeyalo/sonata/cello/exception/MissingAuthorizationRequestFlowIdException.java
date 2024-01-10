package com.odeyalo.sonata.cello.exception;

/**
 * Thrown when request does not contain required 'flow_id' parameter
 */
public class MissingAuthorizationRequestFlowIdException extends RuntimeException {

    public static MissingAuthorizationRequestFlowIdException defaultException() {
        return new MissingAuthorizationRequestFlowIdException();
    }

    public static MissingAuthorizationRequestFlowIdException withCustomMessage(String message) {
        return new MissingAuthorizationRequestFlowIdException(message);
    }

    public static MissingAuthorizationRequestFlowIdException withMessageAndCause(String message, Throwable cause) {
        return new MissingAuthorizationRequestFlowIdException(message, cause);
    }

    public MissingAuthorizationRequestFlowIdException() {
        super();
    }

    public MissingAuthorizationRequestFlowIdException(String message) {
        super(message);
    }

    public MissingAuthorizationRequestFlowIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingAuthorizationRequestFlowIdException(Throwable cause) {
        super(cause);
    }
}
