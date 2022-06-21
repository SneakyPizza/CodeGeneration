package io.swagger.exception;

import io.swagger.exception.custom.*;
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

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDTO> handleNullPointerException(NullPointerException e) {
        ErrorDTO errorDTO = new ErrorDTO(LocalDateTime.now().toString(), e.getMessage(), HttpStatus.BAD_REQUEST.value(), "Null Pointer");
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(NotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO(LocalDateTime.now().toString(), e.getMessage(), HttpStatus.NOT_FOUND.value(), "Not Found");
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
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

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<ErrorDTO> handleForbiddenException(ForbiddenException e) {
        ErrorDTO error = new ErrorDTO(LocalDateTime.now().toString(), e.getMessage(), HttpStatus.FORBIDDEN.value(), "FORBIDDEN");

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
