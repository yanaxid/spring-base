package com.base.spring.config;

import java.io.IOException;
import java.util.List;

import com.base.spring.dto.ErrorDto;
import com.base.spring.dto.MessageResponse;
import com.base.spring.exception.DuplicateResourceException;
import com.base.spring.exception.NotFoundException;
import com.base.spring.util.ErrorDtoFactory;
import com.base.spring.util.ResponseUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.base.spring.enums.MessageKey.*;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final ResponseUtil responseUtil;
    private final ErrorDtoFactory error;

    // --- 1. REQUEST VALIDATION / INPUT (400 Bad Request) ---

    // ex for DTO with @Valid in @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage(), ex); // Log input yang tidak valid dari @RequestBody
        List<ErrorDto> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> new ErrorDto(e.getField(), e.getDefaultMessage()))
                .toList();
        return responseUtil.badRequest(VALIDATION_INVALID_FORMAT, errors);
    }

    // ex for @RequestParam / @PathVariable
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MessageResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("Constraint violation on request param/path variable: {}", ex.getMessage(), ex);
        List<ErrorDto> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> new ErrorDto(v.getPropertyPath().toString(), v.getMessage()))
                .toList();
        return responseUtil.badRequest(VALIDATION_INVALID_FORMAT, errors);
    }

    // Missing required query parameter (e.g., ?page=)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<MessageResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        log.warn("Missing request parameter: {}", ex.getParameterName(), ex);
        ErrorDto errorDto = new ErrorDto(ex.getParameterName(), "parameter is missing");
        return responseUtil.badRequest(VALIDATION_INVALID_FORMAT, List.of(errorDto));
    }

    // Missing required path variable (e.g., /user/{id} but {id} is absent)
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<MessageResponse> handleMissingPathVariable(MissingPathVariableException ex) {
        log.warn("Missing path variable: {}", ex.getVariableName(), ex);
        ErrorDto errorDto = new ErrorDto(ex.getVariableName(), "path variable is missing");
        return responseUtil.badRequest(VALIDATION_INVALID_FORMAT, List.of(errorDto));
    }

    // Type conversion failed (e.g., String provided but Long expected)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MessageResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("Type mismatch for method argument: {}", ex.getMessage(), ex);
        return responseUtil.badRequest(
                VALIDATION_INVALID_FORMAT,
                List.of(ex.getMostSpecificCause().getMessage()));
    }

    // Invalid JSON format (e.g., missing comma or unclosed quote)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MessageResponse> handleJsonParseException(HttpMessageNotReadableException ex) {
        log.warn("Unreadable JSON in request body: {}", ex.getMessage(), ex);
        return responseUtil.badRequest(
                VALIDATION_INVALID_FORMAT,
                List.of(ex.getMostSpecificCause().getMessage()));
    }

    //Thrown duplicate resource
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<MessageResponse> handleDuplicateResource(DuplicateResourceException ex) {
        log.warn("Duplicate resource: {}", ex.getMessage(), ex);
        return responseUtil.badRequest(FAILED_TO_PROCESS, ex.getErrors());
    }

    // Thrown manually for custom validation failures
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument in request: {}", ex.getMessage(), ex);
        return responseUtil.badRequest(
                VALIDATION_INVALID_FORMAT,
                List.of(new ErrorDto("", ex.getMessage())));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<MessageResponse> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Invalid login credentials: {}", ex.getMessage());

        ErrorDto errorDto = error.from("username/password", "Invalid username or password");

        return responseUtil.badRequest(AUTHENTICATION_FAILED, List.of(errorDto));
    }

    // --- 2. ENTITY / DATA ERROR (404 Not Found, 409 Conflict) ---

    // Thrown when JPA fails to find entity (e.g., findById().orElseThrow())
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MessageResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.warn("JPA entity not found: {}", ex.getMessage(), ex);
        ErrorDto err = error.from("id", ex.getMessage());
        return responseUtil.notFound(ERROR_NOT_FOUND, List.of(err));
    }

    // Thrown when custom NotFoundException is raised from service layer
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageResponse> handleNotFoundException(NotFoundException ex) {
        log.warn("Custom NotFoundException: {}", ex.getMessage(), ex);
        return responseUtil.notFound(ERROR_NOT_FOUND, ex.getErrors());
    }

    // Thrown on database constraint violations (e.g. duplicate or foreign key)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<MessageResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation (DB constraint): {}", ex.getMessage(), ex);
        return responseUtil.conflict(ERROR_DATA_CONFLICT);
    }

    // --- 3. SECURITY / AUTHORIZATION ERROR (403 Forbidden) ---

    // Thrown when user lacks permission (e.g. insufficient role)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MessageResponse> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage(), ex);
        return responseUtil.forbidden(ERROR_ACCESS_DENIED);
    }

    // --- 4. METHOD ERROR (405 Method Not Allowed) ---

    // Thrown when HTTP method is not supported by the controller (e.g. POST on a GET endpoint)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MessageResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn("HTTP method not allowed: {}", ex.getMessage(), ex);
        return responseUtil.methodNotAllowed(ERROR_METHOD_NOT_ALLOWED);
    }

    // --- 5. IO / SERVER ERROR (500 Internal Server Error) ---

    // Thrown on I/O errors (e.g. reading files, streams, or multipart upload)
    @ExceptionHandler(IOException.class)
    public ResponseEntity<MessageResponse> handleIOException(IOException ex) {
        log.error("I/O error occurred: {}", ex.getMessage(), ex);
        return responseUtil.internalServerError(ERROR_SERVER);
    }

    // --- 6. GENERIC ERROR (500 Internal Server Error - fallback) ---

    // Fallback for any unhandled exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGenericException(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        return responseUtil.internalServerError(ERROR_SERVER);
    }
}


