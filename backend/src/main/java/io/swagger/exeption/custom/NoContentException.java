package io.swagger.exeption.custom;

public class NoContentException extends RuntimeException {
    public NoContentException(String message) {
        super(message);
    }
}
