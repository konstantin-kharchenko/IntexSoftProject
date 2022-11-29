package by.kharchenko.intexsoftproject.exception;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

public class ExistsException extends Exception{

    @Getter
    List<ObjectError> errors;

    public ExistsException() {
    }

    public ExistsException(String message) {
        super(message);
    }

    public ExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistsException(Throwable cause) {
        super(cause);
    }

    public ExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ExistsException(List<ObjectError> errors) {
        this.errors = errors;
    }
}
