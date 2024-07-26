package pl.dk.aibron_first_task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EventExistsException extends RuntimeException{

    public EventExistsException(String message) {
        super(message);
    }
}
