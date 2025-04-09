package com.base.spring.validator.annotation;

import com.base.spring.validator.validatorimpl.UserDtoValidImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserDtoValidImpl.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserDto {
    String message() default "Invalid dto";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

