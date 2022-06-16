package io.swagger.exception.custom;

public class InvalidTransactionsException extends RuntimeException {
    public InvalidTransactionsException(String message) {
        super(message);
    }
}
