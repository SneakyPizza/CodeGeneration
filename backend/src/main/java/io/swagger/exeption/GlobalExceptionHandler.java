package io.swagger.exeption;

import io.swagger.exeption.custom.InvalidTransactionsException;
import io.swagger.exeption.custom.TransactionDeniedException;
import io.swagger.exeption.custom.UnauthorizedException;
import io.swagger.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorDTO errorDTO = new ErrorDTO(LocalDateTime.now().toString(), e.getMessage(), HttpStatus.BAD_REQUEST.value(), "Illegal Argument");
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDTO> handleUnauthorizedException(UnauthorizedException e) {
        ErrorDTO errorDTO = new ErrorDTO(LocalDateTime.now().toString(), e.getMessage(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTransactionsException.class)
    public ResponseEntity<ErrorDTO> handleInvalidTransactionsException(InvalidTransactionsException e) {
        ErrorDTO errorDTO = new ErrorDTO(LocalDateTime.now().toString(), e.getMessage(), HttpStatus.BAD_REQUEST.value(), "Invalid Transactions");
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionDeniedException.class)
    public ResponseEntity<ErrorDTO> handleTransactionDeniedException(TransactionDeniedException e) {
        ErrorDTO errorDTO = new ErrorDTO(LocalDateTime.now().toString(), e.getMessage(), HttpStatus.BAD_REQUEST.value(), "Transaction Denied");
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception e) {
        ErrorDTO error = new ErrorDTO(LocalDateTime.now().toString(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR");

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
