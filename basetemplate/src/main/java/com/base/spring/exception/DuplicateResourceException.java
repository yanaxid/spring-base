package com.base.spring.exception;

import com.base.spring.dto.ErrorDto;
import lombok.Getter;

import java.util.List;

@Getter
public class DuplicateResourceException extends RuntimeException {

    private final transient List<ErrorDto> errors;

    public DuplicateResourceException(List<ErrorDto> errors) {
        super("Duplicate resource error");
        this.errors = errors;
    }

    public DuplicateResourceException(String field, String message) {
        super(message);
        this.errors = List.of(new ErrorDto(field, message));
    }
}

