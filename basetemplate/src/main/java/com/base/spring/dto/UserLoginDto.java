package com.base.spring.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
@Getter
public class UserLoginDto {

    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank(message = "{password.not.blank}")
    @Size(min = 3, message = "{password.length}")
    private String password;
}

