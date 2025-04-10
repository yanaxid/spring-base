package com.base.spring.enums;


import lombok.Getter;

@Getter
public enum MessageKey {

    // --- SUCCESS MESSAGES ---
    SUCCESS_SAVE_DATA("success.save.data"),
    SUCCESS_GET_DATA("success.get.data"),
    SUCCESS_NO_CHANGE("success.no.change"),
    SUCCESS_UPDATE_DATA("success.update.data"),
    SUCCESS_DELETE_DATA("success.delete.data"),

    // --- VALIDATION ERRORS ---
    VALIDATION_INVALID_FORMAT("validation.invalid.format"),


    // --- GENERAL ERRORS ---
    ERROR_SERVER("error.server"),
    ERROR_NOT_FOUND("error.not.found"),
    FAILED_TO_PROCESS("failed.to.process"),
    ERROR_DATA_CONFLICT("error.data.conflict"),
    ERROR_ACCESS_DENIED("error.access.denied"),
    ERROR_METHOD_NOT_ALLOWED("error.method.not.allowed"),

    // --- AUTH MESSAGES ---
    SUCCESS_SIGNUP("success.signup"),
    SUCCESS_SIGNIN("success.signin"),
    SUCCESS_LOGOUT("success.logout"),
    TOKEN_BLOCKED("token.blocked"),
    AUTHENTICATION_FAILED("authentication.failed"),
    UNAUTHORIZED("error.unauthorized"),

    // --- DOMAIN SPECIFIC ---
    NOT_FOUND("not.found");

    private final String key;

    MessageKey(String key) {
        this.key = key;
    }

}
