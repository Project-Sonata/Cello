package com.odeyalo.sonata.cello.exception;

public final class NotSupportedOauth2ProviderException extends RuntimeException {

    public static NotSupportedOauth2ProviderException defaultException() {
        return new NotSupportedOauth2ProviderException();
    }

    public static NotSupportedOauth2ProviderException withCustomMessage(String message, Object... args) {
        return new NotSupportedOauth2ProviderException(message, args);
    }

    public static NotSupportedOauth2ProviderException withMessageAndCause(String message, Throwable cause) {
        return new NotSupportedOauth2ProviderException(message, cause);
    }

    public NotSupportedOauth2ProviderException() {
        super();
    }

    public NotSupportedOauth2ProviderException(String message, Object... args) {
        super(String.format(message, args));
    }

    public NotSupportedOauth2ProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportedOauth2ProviderException(Throwable cause) {
        super(cause);
    }
}
