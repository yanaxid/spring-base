package com.base.spring.util;

import com.base.spring.dto.ErrorDto;
import com.base.spring.enums.MessageKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ErrorDtoFactory {

    private final MessageUtil messageUtil;


    // --- 1. CREATE ERROR DTO FROM STRING REASON ---
    public ErrorDto from(String field, String reason, Object... args) {
        return new ErrorDto(field, messageUtil.get(reason, args));
    }

    // --- 2. CREATE ERROR DTO FROM ENUM MessageKey ---
    public ErrorDto from(String field, MessageKey reason, Object... args) {
        return from(field, messageUtil.get(reason.getKey(), args));
    }
}


