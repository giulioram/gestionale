package com.giulioram.gestionale.system.exception;

import com.giulioram.gestionale.system.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleObjectNotFoundException(Exception ex) {
        return new Result(false, HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationException(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach((error) -> {
            String key = ((FieldError)error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });
        return new Result(false, HttpStatus.BAD_REQUEST.value(), "Provided arguments are invalid, see data for details.", map);
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAuthenticationException(Exception ex) {
        return new Result(false, HttpStatus.UNAUTHORIZED.value(), "username or password is incorrect", ex.getMessage());
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAccountStatusException(AccountStatusException ex) {
        return new Result(false, HttpStatus.UNAUTHORIZED.value(), "User account is abnormal.", ex.getMessage());
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleInvalidBearerTokenException(InvalidBearerTokenException ex) {
        return new Result(false, HttpStatus.UNAUTHORIZED.value(), "The access token provided is expired, revoked, malformed, or invalid for other reasons.", ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Result handleAccessDeniedException(AccessDeniedException ex) {
        return new Result(false, HttpStatus.FORBIDDEN.value(), "No permission.", ex.getMessage());
    }

    /**
     * Handles exceptions of type {@link Exception} that are not explicitly handled by other methods.
     * Returns a standardized error response indicating an internal server error.
     *
     * @param ex the exception to handle
     * @return a {@link Result} object representing the error response, including the error message
     *         and HTTP status code (500 - Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Result handleOtherException(Exception ex) {
        return new Result(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "A server internal error occurs.", ex.getMessage());
    }
}
