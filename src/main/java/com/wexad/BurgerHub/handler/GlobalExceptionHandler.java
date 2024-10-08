package com.wexad.BurgerHub.handler;

import com.wexad.BurgerHub.dto.ErrorResponseDTO;
import com.wexad.BurgerHub.handler.exceptions.UserDeletedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "Database error occurred. Please check the data and try again.";

        if (ex.getMessage().contains("auth_user_username_key")) {
            message = "Username already exists.";
        } else if (ex.getMessage().contains("auth_user_email_key")) {
            message = "Email address already exists.";
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(message));
    }

    @ExceptionHandler(UserDeletedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponseDTO> handleUserDeletedException(UserDeletedException ex) {
        String message = "User is deleted.";
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDTO(message));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException ex) {
        String message = "Invalid username or password.";
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(message));
    }
}

