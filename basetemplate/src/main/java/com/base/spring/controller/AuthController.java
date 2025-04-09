package com.base.spring.controller;

import com.base.spring.dto.MessageResponse;
import com.base.spring.dto.UserLoginDto;
import com.base.spring.dto.UserSignUpDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.base.spring.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<MessageResponse> signIn(@Valid @RequestBody UserLoginDto userLoginDto) {
        return userService.signIn(userLoginDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signUp(@Valid @RequestBody UserSignUpDto userSignUpDto) {
        return userService.signUp(userSignUpDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logOut(HttpServletRequest request) {
        return userService.logOut(request);
    }

}

