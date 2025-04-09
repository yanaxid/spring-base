package com.base.spring.config;

import java.io.IOException;
import java.util.List;

import com.base.spring.dto.ErrorDto;
import com.base.spring.exception.NotFoundException;
import com.base.spring.util.ErrorDtoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.base.spring.dto.MessageResponse;
import static com.base.spring.enums.MessageKey.*;
import com.base.spring.util.ResponseUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;


@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

   private final ResponseUtil responseUtil;
   private final ErrorDtoFactory error;

   // validasi dto field
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<MessageResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
      List<ErrorDto> errors = ex.getBindingResult()
              .getFieldErrors()
              .stream()
              .map(e -> new ErrorDto(
                      e.getField(),
                      e.getDefaultMessage()
              ))
              .toList();

      return responseUtil.badRequest(VALIDATION_INVALID_FORMAT, errors);
   }


   // untuk validasi params
   @ExceptionHandler(ConstraintViolationException.class)
   public ResponseEntity<MessageResponse> handleConstraintViolationException(ConstraintViolationException ex) {
      List<ErrorDto> errors = ex.getConstraintViolations()
              .stream()
              .map(violation -> new ErrorDto(
                      violation.getPropertyPath().toString(), // Nama field
                      violation.getMessage()                  // Pesan default validasinya
              ))
              .toList();

      return responseUtil.badRequest(VALIDATION_INVALID_FORMAT, errors);
   }


   @ExceptionHandler(EntityNotFoundException.class)
   public ResponseEntity<MessageResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
      // Asumsikan ex.getMessage() itu key dari messages.properties, misalnya "product.not.found"
      ErrorDto err = error.from("id", ex.getMessage()); // atau ganti field sesuai konteks
      return responseUtil.notFound(ERROR_NOT_FOUND, List.of(err));
   }


   @ExceptionHandler(NotFoundException.class)
   public ResponseEntity<MessageResponse> handleNotFoundException(NotFoundException ex) {
      return responseUtil.notFound(ERROR_NOT_FOUND, ex.getErrors());
   }


   @ExceptionHandler(Exception.class)
   public ResponseEntity<MessageResponse> handleGenericException(Exception ex) {
      log.error("Unexpected error occurred", ex); // log sebagai error, bukan info
      return responseUtil.internalServerError(ERROR_SERVER);
   }


   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<MessageResponse> handleIllegalArgumentException(IllegalArgumentException ex) {

      return responseUtil.badRequest(
            VALIDATION_INVALID_FORMAT,
            List.of(new ErrorDto("", ex.getMessage())));
   }

   // format json salah
   @ExceptionHandler(HttpMessageNotReadableException.class)
   public ResponseEntity<MessageResponse> handleJsonParseException(HttpMessageNotReadableException ex) {

      return responseUtil.badRequest(
            VALIDATION_INVALID_FORMAT,
            List.of(ex.getMostSpecificCause().getMessage()));
   }

   // parameter salah tipe
   @ExceptionHandler(MethodArgumentTypeMismatchException.class)
   public ResponseEntity<MessageResponse> handleMethodArgumentTypeMismatchException(
         MethodArgumentTypeMismatchException ex) {

      return responseUtil.badRequest(
            VALIDATION_INVALID_FORMAT,
            List.of(ex.getMostSpecificCause().getMessage()));
   }


   @ExceptionHandler(IOException.class)
   public ResponseEntity<MessageResponse> handleIOException(IOException ex) {
      log.error("IO Exception occurred", ex);
      return responseUtil.internalServerError(ERROR_SERVER);
   }


   @ExceptionHandler(DataIntegrityViolationException.class)
   public ResponseEntity<MessageResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
      log.warn("Data integrity violation: {}", ex.getMessage());
      return responseUtil.conflict(ERROR_DATA_CONFLICT);
   }



}
