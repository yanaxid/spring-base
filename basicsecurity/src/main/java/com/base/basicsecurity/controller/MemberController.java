package com.base.basicsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

   @GetMapping("/profile")
   public ResponseEntity<String> getMember() {
      return ResponseEntity.ok("hi member");
   }
}
