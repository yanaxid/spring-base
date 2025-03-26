package com.base.basicsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.base.basicsecurity.dto.UserDto;
import com.base.basicsecurity.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

   private final UserService userService;

   @PostMapping("/register")
   public ResponseEntity<String> register(@RequestBody UserDto userDto) {
      return userService.register(userDto);

   }
}
