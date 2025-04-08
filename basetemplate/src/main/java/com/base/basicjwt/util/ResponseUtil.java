package com.base.basicjwt.util;


import com.base.basicjwt.dto.ErrorDto;
import com.base.basicjwt.enums.MessageKey;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.base.basicjwt.dto.MessageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseUtil {

   private final MessageUtil messageUtil;

   private ResponseEntity<MessageResponse> buildResponse(
         HttpStatus status,
         String messageKey,
         Object data,
         List<ErrorDto> error,
         MessageResponse.Meta meta,
         String... args) {

      String message = messageUtil.get(messageKey, (Object[]) args);
      return ResponseEntity.status(status).body(new MessageResponse(
            message,
            status.value(),
            status.getReasonPhrase(),
            data,
            error,
            meta));
   }

   public ResponseEntity<MessageResponse> notFound(String messageKey, String... args) {
      return buildResponse(HttpStatus.NOT_FOUND, messageKey, null, null, null, args);
   }

   public ResponseEntity<MessageResponse> successWithData(String messageKey, Object data, String... args) {
      return buildResponse(HttpStatus.OK, messageKey, data, null, null, args);
   }

   public ResponseEntity<MessageResponse> successWithData(MessageKey messageKey, Object data, String... args) {
      return successWithData(messageKey.getKey(), data, args);
   }

   public ResponseEntity<MessageResponse> success(String messageKey, String... args) {
      return buildResponse(HttpStatus.OK, messageKey, null, null, null, args);
   }

   public ResponseEntity<MessageResponse> success(MessageKey messageKey, String... args) {
      return success(messageKey.getKey(), args);
   }


   public ResponseEntity<MessageResponse> successWithDataAndMeta(String messageKey, Object data,
         MessageResponse.Meta meta) {
      return buildResponse(HttpStatus.OK, messageKey, data, null, meta);
   }

   public ResponseEntity<MessageResponse> badRequest(String messageKey, List<ErrorDto> errors, String... args) {
      return buildResponse(HttpStatus.BAD_REQUEST, messageKey, null, errors, null, args);
   }

   public ResponseEntity<MessageResponse> badRequest(MessageKey messageKey, List<ErrorDto> errors, String... args) {
      return badRequest(messageKey.getKey(), errors, args);
   }

   public ResponseEntity<MessageResponse> internalServerError(String messageKey) {
      return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, messageKey, null, null, null);
   }

   public ResponseEntity<MessageResponse> internalServerError(MessageKey messageKey) {
      return internalServerError(messageKey.getKey());
   }




}
