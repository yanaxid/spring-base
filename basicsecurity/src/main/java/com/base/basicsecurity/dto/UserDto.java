package com.base.basicsecurity.dto;

import lombok.Data;

@Data
public class UserDto {

   private String fullname;
   private String email;
   private String password;
   private String role;
}
