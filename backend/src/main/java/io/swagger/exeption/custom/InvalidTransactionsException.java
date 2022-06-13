package io.swagger.exeption.custom;

public class InvalidTransactionsException extends RuntimeException {
    public InvalidTransactionsException(String message) {
        super(message);
    }
}
