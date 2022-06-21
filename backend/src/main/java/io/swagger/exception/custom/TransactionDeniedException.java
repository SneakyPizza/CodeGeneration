package io.swagger.exception.custom;

public class TransactionDeniedException extends RuntimeException {
    public TransactionDeniedException(String message) {
        super(message);
    }
}
