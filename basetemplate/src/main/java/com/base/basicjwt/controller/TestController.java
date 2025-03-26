package com.base.basicjwt.controller;



import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {
   @GetMapping("/all")
   public String allAccess() {
      return "Public Content.";
   }

   @PreAuthorize("hasAuthority('USER')")
   @GetMapping("/user")
   public String userAccess() {
      return "User !!! Content.";
   }

   
   @PreAuthorize("hasAuthority('ADMIN')")
   @GetMapping("/admin")
   public String userAccess1() {
      return "ADMIN !!! Content.";
   }
}