package dev.sorokin.eventmanager.controller.exceptions;

import dev.sorokin.eventmanager.domain.exception.LocationNotFoundException;
import dev.sorokin.eventmanager.domain.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ) {
        List<ErrorMessage> messages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new ErrorMessage(error.getField(), error.getDefaultMessage()))
                .toList();

        var body = new ErrorMessageResponse(
                "Bad request",
                messages,
                LocalDateTime.now()
        );
        log.warn("Bad request exception: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleException(
            Exception ex
    ) {
        var body = new ErrorMessageResponse(
                "Internal server error",
                List.of(new ErrorMessage("Error", "Internal server error")),
                LocalDateTime.now()
        );
        log.error("Internal server error exception: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler({LocationNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ErrorMessageResponse> handleNotFoundException(
            Exception ex
    ) {
        var body = new ErrorMessageResponse(
                "Not found",
                List.of(new ErrorMessage("Error", ex.getMessage())),
                LocalDateTime.now()
        );
        log.warn("Not found exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
