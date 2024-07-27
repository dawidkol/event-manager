package pl.dk.aibron_first_task.error_handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.dk.aibron_first_task.event.Event;
import pl.dk.aibron_first_task.event.dtos.SaveEventDto;

import java.util.List;

@RestControllerAdvice
class RestControllerErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<MethodArgumentNotValidWrapper> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Object target = ex.getBindingResult().getTarget();
        String formatted = checkAndReturnInstance(target);
        if (!ex.getBindingResult().hasFieldErrors()) {
            return ex.getAllErrors()
                    .stream()
                    .map(e -> new MethodArgumentNotValidWrapper(formatted, e.getDefaultMessage()))
                    .toList();
        } else {
            return ex.getBindingResult().getFieldErrors()
                    .stream()
                    .map(fieldError -> new MethodArgumentNotValidWrapper(fieldError.getField(), fieldError.getDefaultMessage()))
                    .toList();
        }
    }

    private String checkAndReturnInstance(Object target) {
        String field = "%s, %s";
        if (target instanceof SaveEventDto || target instanceof Event) {
            return field.formatted("eventStart", "eventEnd");
        }
        return "";
    }

    record MethodArgumentNotValidWrapper(String field, String message) {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<ConstraintViolationWrapper> handleConstraintViolationException(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                .stream()
                .map(fieldError -> new ConstraintViolationWrapper(
                        fieldError.getPropertyPath().toString().split("\\.")[1],
                        fieldError.getInvalidValue().toString(),
                        fieldError.getMessage()))
                .toList();
    }

    record ConstraintViolationWrapper(String parameter, String value, String message) {
    }
}

