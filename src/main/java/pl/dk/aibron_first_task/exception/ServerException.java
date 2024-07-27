package pl.dk.aibron_first_task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Server error, try again later")
public class ServerException extends RuntimeException {

}
