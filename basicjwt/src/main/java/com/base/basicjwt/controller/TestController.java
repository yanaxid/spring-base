package com.base.basicjwt.controller;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TestController {
   @GetMapping("/all")
   public String allAccess() {
      return "Public Content.";
   }

   @GetMapping("/user")
   public String userAccess() {
      return "User !!! Content.";
   }


   @GetMapping("/admin")
   public String userAccess1() {
      return "ADMIN !!! Content.";
   }
}