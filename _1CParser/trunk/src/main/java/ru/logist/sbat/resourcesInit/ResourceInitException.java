package ru.logist.sbat.resourcesInit;

public class ResourceInitException extends Exception {
    public ResourceInitException() {
    }

    public ResourceInitException(String message) {
        super(message);
    }

    public ResourceInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceInitException(Throwable cause) {
        super(cause);
    }
}
