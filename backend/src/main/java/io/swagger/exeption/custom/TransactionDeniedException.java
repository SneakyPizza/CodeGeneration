package io.swagger.exeption.custom;

public class TransactionDeniedException extends RuntimeException {
    public TransactionDeniedException(String message) {
        super(message);
    }
}
