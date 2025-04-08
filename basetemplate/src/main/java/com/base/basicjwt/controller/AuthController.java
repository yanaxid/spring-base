package com.base.basicjwt.controller;

import com.base.basicjwt.dto.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.base.basicjwt.dto.UserDto;
import com.base.basicjwt.model.User;
import com.base.basicjwt.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<MessageResponse> signIn(@RequestBody UserDto userDto) {
        return userService.signIn(userDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signUp(@RequestBody User user) {
        return userService.signUp(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logOut(HttpServletRequest request) {
        return userService.logOut(request);
    }

}

