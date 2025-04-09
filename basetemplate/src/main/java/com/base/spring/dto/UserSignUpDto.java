package com.base.spring.dto;

import com.base.spring.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
@Getter
public class UserSignUpDto {

    @NotBlank
    private String fullname;

    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private Role role;
}
