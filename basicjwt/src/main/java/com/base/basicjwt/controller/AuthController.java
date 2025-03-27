package com.base.basicjwt.controller;

import com.base.basicjwt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.base.basicjwt.dto.UserDto;
import com.base.basicjwt.model.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

   private final UserService userService;

   @PostMapping("/signin")
   public ResponseEntity<String> signIn(@RequestBody UserDto userDto) {
      return userService.signIn(userDto);
   }

   @PostMapping("/signup")
   public String signUp(@RequestBody User user) {
      return userService.signUp(user);
   }

   @PostMapping("/logout")
   public ResponseEntity<String> logOut(HttpServletRequest request) {
      return userService.logOut(request);
   }

}
