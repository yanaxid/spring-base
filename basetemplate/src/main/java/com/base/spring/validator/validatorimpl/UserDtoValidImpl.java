package com.base.spring.validator.validatorimpl;

import com.base.spring.dto.UserLoginDto;
import com.base.spring.validator.annotation.ValidUserDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;


//SAMPLE FOR MAKE CUSTOM VALIDATION WITH ANOTAION
@Component
public class UserDtoValidImpl implements ConstraintValidator<ValidUserDto, UserLoginDto> {

    private static final String USERNAME = "username";
    private static final String PASSWORD_NOT_BLANK = "{password.not.blank}";

    @Override
    public boolean isValid(UserLoginDto dto, ConstraintValidatorContext context) {
        boolean isValid = true;

        // Disable default constraint violation
        context.disableDefaultConstraintViolation();

        String username = dto.getUsername();

        if (username == null || username.trim().isEmpty()) {
            context.buildConstraintViolationWithTemplate(PASSWORD_NOT_BLANK)
                    .addPropertyNode(USERNAME)
                    .addConstraintViolation();
            isValid = false;
        } else {
            if (username.length() < 3) {
                context.buildConstraintViolationWithTemplate(PASSWORD_NOT_BLANK)
                        .addPropertyNode(USERNAME)
                        .addConstraintViolation();
                isValid = false;
            }

            if (username.matches(".*\\d.*")) {
                context.buildConstraintViolationWithTemplate(PASSWORD_NOT_BLANK)
                        .addPropertyNode(USERNAME)
                        .addConstraintViolation();
                isValid = false;
            }
        }

        return isValid;
    }
}


