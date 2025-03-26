package com.base.basicjwt.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.base.basicjwt.enums.Role;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "app_user")
@EntityListeners(AuditingEntityListener.class)
@Builder
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String fullname;

   @Column(unique = true, nullable = false)
   private String username;

   @Column(unique = true, nullable = false)
   private String email;

   @Column(nullable = false)
   private String password;

   @Enumerated(EnumType.STRING)
   private Role role;

   @CreatedDate
   @Column(name = "created_date", updatable = false)
   private LocalDateTime createdDate;

   @LastModifiedDate
   @Column(name = "modified_date")
   private LocalDateTime modifiedDate;

}
