package io.swagger.model.entities;


public class TransactionValidation {
    //enum with transaction validation
    public enum TransactionValidationStatus {
        VALID,
        UNAUTHORIZED,
        INVALID_PIN,
        NOT_ACTIVE,
        NOT_ALLOWED,
        INSUFFICIENT_FUNDS,
        TRANSACTION_LIMIT_EXCEEDED,
        DAILY_LIMIT_EXCEEDED,

    }
    //this is a transaction validation response class used only used serverside
    public Boolean isValid;
    public String message;
    public TransactionValidationStatus status;
    public TransactionValidation(Boolean isValid, String message, TransactionValidationStatus status) {
        this.isValid = isValid;
        this.message = message;
        this.status = status;
    }
}
