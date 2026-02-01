package com.look_finder.errors;

import com.look_finder.DTO.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AGlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "EMAIL_EXISTS",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(UserIsntRegistratedException.class)
    public ResponseEntity<?> handleUserIsntRegistrated(UserIsntRegistratedException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "EMAIL_EXISTS",
                        "message", ex.getMessage()
                        )
                );
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorDTO> handleAppException(AppException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorDTO(ex.getMessage(), ex.getCode()));
    }
}
