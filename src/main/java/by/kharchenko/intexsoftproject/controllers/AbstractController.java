package by.kharchenko.intexsoftproject.controllers;

import by.kharchenko.intexsoftproject.exception.ExistsException;
import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.message.ValidationMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationMessage>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ValidationMessage> validationMessageList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationMessageList.add(new ValidationMessage(fieldName, errorMessage));
        });
        return new ResponseEntity<>(validationMessageList, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> serviceException(ServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> serviceException(UsernameNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExistsException.class)
    public ResponseEntity<List<ValidationMessage>> existsException(ExistsException ex) {
        List<ValidationMessage> existsExceptions = new ArrayList<>();
        ex.getErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            existsExceptions.add(new ValidationMessage(fieldName, errorMessage));
        });
        return new ResponseEntity<>(existsExceptions, HttpStatus.BAD_REQUEST);
    }
}
