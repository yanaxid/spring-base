package com.base.spring.exception;


import com.base.spring.dto.ErrorDto;
import lombok.Getter;

import java.util.List;

@Getter
public class NotFoundException extends RuntimeException {

    private final transient List<ErrorDto> errors;

    public NotFoundException(String field,  String message) {
        super(message);
        this.errors = List.of(new ErrorDto(field, message));
    }

    public NotFoundException(List<ErrorDto> errors) {
        super("Not Found");
        this.errors = errors;
    }
}
