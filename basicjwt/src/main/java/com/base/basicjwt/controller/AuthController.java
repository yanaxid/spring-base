package com.base.basicjwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.base.basicjwt.dto.LoginResponse;
import com.base.basicjwt.dto.UserDto;
import com.base.basicjwt.jwt.JwtUtil;
import com.base.basicjwt.model.User;
import com.base.basicjwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

   private final AuthenticationManager authenticationManager;
   private final UserRepository userRepository;
   private final PasswordEncoder encoder;
   private final JwtUtil jwtUtils;

   @PostMapping("/signin")
   public ResponseEntity<LoginResponse> authenticateUser(@RequestBody UserDto userDto) {
      Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                  userDto.getUsername(),
                  userDto.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();

      User user = userRepository.findByUsername(userDetails.getUsername());

      String role = user.getRole().name();

      LoginResponse response = new LoginResponse();
      response.setJwtToken(jwtUtils.generateToken(userDetails));
      response.setUsername(userDetails.getUsername());
      response.setRole(role);
      return ResponseEntity.ok(response);
   }

   @PostMapping("/signup")

   public String registerUser(@RequestBody User user) {
      if (userRepository.existsByUsername(user.getUsername())) {
         return "Error: Username is already taken!";
      }

      User newUser = User.builder()
            .fullname("")
            .username(user.getUsername())
            .email(user.getEmail())
            .password(encoder.encode(user.getPassword()))
            .role(user.getRole())
            .build();

      userRepository.save(newUser);
      return "User registered successfully!";
   }

   @PostMapping("/logout")
   public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
      if (token == null || !token.startsWith("Bearer ")) {
         return ResponseEntity.badRequest().body("Invalid token format!");
      }

      token = token.substring(7);

      if (!jwtUtils.validateJwtToken(token)) {
         return ResponseEntity.badRequest().body("Invalid or expired token!");
      }

      if (jwtUtils.isTokenBlacklisted(token)) {
         return ResponseEntity.badRequest().body("Token already logged out!");
      }

      jwtUtils.addToBlacklist(token);
      return ResponseEntity.ok("User logged out successfully!");
   }

}
