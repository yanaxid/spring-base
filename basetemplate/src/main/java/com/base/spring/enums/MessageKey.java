package com.base.spring.enums;

public enum MessageKey {
    ERROR_SERVER("error.server"),
    SUCCESS_SAVE_DATA("success.save.data"),
    SUCCESS_GET_DATA("success.get.data"),
    ERROR_NOT_FOUND("error.not.found"),
    SUCCESS_NO_CHANGE("success.no.change"),
    SUCCESS_UPDATE_DATA("success.update.data"),
    SUCCESS_DELETE_DATA("success.delete.data"),
    VALIDATION_REQUIRED("validation.required"),
    VALIDATION_INVALID_FORMAT("validation.invalid.format"),
    VALIDATION_INVALID_LENGTH("validation.invalid.length"),
    ERROR_GENERAL("error.general"),


    //   GENERAL ERROR
    FAILED_TO_PROCESS("failed.to.process"),

    //  AUTH
    SUCCESS_SIGNUP("success.signup"),
    SUCCESS_SIGNIN("success.signin"),
    SUCCESS_LOGOUT("success.logout"),
    TOKEN_BLOCKED("token.blocked"),


    //   PRODUCT
    NOT_FOUND("not.found"),

    ERROR_DATA_CONFLICT("error.data.conflict");


    private final String key;

    MessageKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}