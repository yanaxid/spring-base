package com.base.spring.dto;



import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {
   private String message;
   private int statusCode;
   private String status;
   private Object data;
   private Object errors;
   private Meta meta;

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   @Builder
   public static class Meta {
      private Long total;
      private int perPage;
      private int currentPage;
      private int lastPage;

      public int getCurrentPage() {
         return currentPage + 1;
      }
   }

   public MessageResponse(String message, int statusCode, String status) {
      this.message = message;
      this.statusCode = statusCode;
      this.status = status;
   }

   public MessageResponse(String message, int statusCode, String status, Object data, Object errors) {
      this.message = message;
      this.statusCode = statusCode;
      this.status = status;
      this.data = data;
      this.errors = errors;
   }

}
