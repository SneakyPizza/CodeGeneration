package io.swagger.model.entities;

public class TransactionValidation {
    //this is a transaction validation response class used only used serverside
    public Boolean isValid;
    public String message;
    public TransactionValidation(Boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }
}
