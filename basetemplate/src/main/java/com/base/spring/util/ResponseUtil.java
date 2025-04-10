package com.base.spring.util;

import com.base.spring.enums.MessageKey;
import com.base.spring.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseUtil {

    private final MessageUtil messageUtil;

    // --- 1. CORE METHOD ---
    // This is the central method used to build all standardized API responses.

    private ResponseEntity<MessageResponse> buildResponse(HttpStatus status, String messageKey, Object data, Object error, MessageResponse.Meta meta, String... args) {
        String message = messageUtil.get(messageKey, (Object[]) args);
        return ResponseEntity.status(status).body(new MessageResponse(
                message,
                status.value(),
                status.getReasonPhrase(),
                data,
                error,
                meta));
    }

    // --- 2. SUCCESS RESPONSES (200 OK) ---
    // Used when the request is successfully processed.

    public ResponseEntity<MessageResponse> success(String messageKey, String... args) {
        return buildResponse(HttpStatus.OK, messageKey, null, null, null, args);
    }

    public ResponseEntity<MessageResponse> success(MessageKey messageKey, String... args) {
        return success(messageKey.getKey(), args);
    }

    public ResponseEntity<MessageResponse> successWithData(String messageKey, Object data, String... args) {
        return buildResponse(HttpStatus.OK, messageKey, data, null, null, args);
    }

    public ResponseEntity<MessageResponse> successWithData(MessageKey messageKey, Object data, String... args) {
        return successWithData(messageKey.getKey(), data, args);
    }

    public ResponseEntity<MessageResponse> successWithDataAndMeta(String messageKey, Object data, MessageResponse.Meta meta) {
        return buildResponse(HttpStatus.OK, messageKey, data, null, meta);
    }

    public ResponseEntity<MessageResponse> successWithDataAndMeta(MessageKey messageKey, Object data, MessageResponse.Meta meta) {
        return successWithDataAndMeta(messageKey.getKey(), data, meta);
    }

    // --- 3. BAD REQUEST RESPONSES (400) ---
    // Used when request validation fails or contains invalid parameters.

    public ResponseEntity<MessageResponse> badRequest(String messageKey, Object errors, String... args) {
        return buildResponse(HttpStatus.BAD_REQUEST, messageKey, null, errors, null, args);
    }

    public ResponseEntity<MessageResponse> badRequest(MessageKey messageKey, Object errors, String... args) {
        return badRequest(messageKey.getKey(), errors, args);
    }

    // --- 4. NOT FOUND RESPONSES (404) ---
    // Used when requested data is not found.

    public ResponseEntity<MessageResponse> notFound(String messageKey, Object errors, String... args) {
        return buildResponse(HttpStatus.NOT_FOUND, messageKey, null, errors, null, args);
    }

    public ResponseEntity<MessageResponse> notFound(MessageKey messageKey, Object errors, String... args) {
        return notFound(messageKey.getKey(), errors, args);
    }

    // --- 5. FORBIDDEN RESPONSES (403) ---
    // Used when the user does not have permission to access the resource.

    public ResponseEntity<MessageResponse> forbidden(String messageKey, String... args) {
        return buildResponse(HttpStatus.FORBIDDEN, messageKey, null, null, null, args);
    }

    public ResponseEntity<MessageResponse> forbidden(MessageKey messageKey, String... args) {
        return forbidden(messageKey.getKey(), args);
    }

    // --- 6. METHOD NOT ALLOWED RESPONSES (405) ---
    // Used when the HTTP method is not supported for the requested resource.

    public ResponseEntity<MessageResponse> methodNotAllowed(String messageKey, String... args) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, messageKey, null, null, null, args);
    }

    public ResponseEntity<MessageResponse> methodNotAllowed(MessageKey messageKey, String... args) {
        return methodNotAllowed(messageKey.getKey(), args);
    }

    // --- 7. CONFLICT RESPONSES (409) ---
    // Used when there is a conflict in the request (e.g. duplicate data).

    public ResponseEntity<MessageResponse> conflict(String messageKey, Object errors, String... args) {
        return buildResponse(HttpStatus.CONFLICT, messageKey, null, errors, null, args);
    }

    public ResponseEntity<MessageResponse> conflict(MessageKey messageKey, Object errors, String... args) {
        return conflict(messageKey.getKey(), errors, args);
    }

    public ResponseEntity<MessageResponse> conflict(String messageKey, String... args) {
        return buildResponse(HttpStatus.CONFLICT, messageKey, null, null, null, args);
    }

    public ResponseEntity<MessageResponse> conflict(MessageKey messageKey, String... args) {
        return conflict(messageKey.getKey(), args);
    }

    // --- 8. INTERNAL SERVER ERROR RESPONSES (500) ---
    // Used when an unexpected error occurs on the server side.

    public ResponseEntity<MessageResponse> internalServerError(String messageKey) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, messageKey, null, null, null);
    }

    public ResponseEntity<MessageResponse> internalServerError(MessageKey messageKey) {
        return internalServerError(messageKey.getKey());
    }

    // --- 9. UNAUTHORIZED RESPONSES (401) ---
    // Used when authentication is required or token is invalid.

    public ResponseEntity<MessageResponse> unauthorized(String messageKey,  String... args) {
        return buildResponse(HttpStatus.UNAUTHORIZED, messageKey, null, null, null, args);
    }


    public ResponseEntity<MessageResponse> unauthorized(MessageKey messageKey, String... args) {
        return unauthorized(messageKey.getKey(), args);
    }
}
