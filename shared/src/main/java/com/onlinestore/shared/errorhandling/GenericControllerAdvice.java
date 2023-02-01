package com.onlinestore.shared.errorhandling;

import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GenericControllerAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    public final ResponseEntity<ErrorMessage> handleValidationExceptions(Exception ex) {
        String errorMessage;
        if (ex instanceof ValidationException) {
            errorMessage = ex.getMessage();
        } else {
            errorMessage = ((MethodArgumentNotValidException) ex).getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(ex.getMessage());
        }

        return ResponseEntity.badRequest().body(ErrorMessage.of(HttpStatus.BAD_REQUEST.name(), errorMessage));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<ErrorMessage> handleNotFoundException(NoSuchElementException ex) {
        return ResponseEntity.badRequest()
            .body(ErrorMessage.of(HttpStatus.BAD_REQUEST.name(), ex.getMessage()));
    }

    @ExceptionHandler(CommandExecutionException.class)
    public final ResponseEntity<ErrorMessage> handleCommandExecutionException(CommandExecutionException ex) {
        return ResponseEntity.internalServerError()
            .body(ErrorMessage.of(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorMessage> handleOtherErrors(Exception ex) {
        return ResponseEntity.internalServerError()
            .body(ErrorMessage.of(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage()));
    }

}
