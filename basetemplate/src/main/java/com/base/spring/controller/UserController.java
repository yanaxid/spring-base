package com.base.spring.controller;


import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

   @GetMapping("/user")
   public String userAccess() {
      return "User !!! Content.";
   }
}