package com.base.basicjwt.enums;

public enum MessageKey {
    ERROR_SERVER("error.server"),
    SUCCESS_SAVE_DATA("success.save.data"),
    SUCCESS_GET_DATA("success.get.data"),
    ERROR_NOT_FOUND("error.not.found"),
    EMAIL_DUPLICATE("email.duplicate"),
    ERROR_NO_CHANGE("error.no.change"),
    SUCCESS_UPDATE_DATA("success.update.data"),
    SUCCESS_DELETE_DATA("success.delete.data"),
    VALIDATION_REQUIRED("validation.required"),
    VALIDATION_INVALID_FORMAT("validation.invalid.format"),
    VALIDATION_INVALID_FORMAT_VAR("validation.invalid.format.var"),
    VALIDATION_INVALID_LENGTH("validation.invalid.length"),
    ISBN_DUPLICATE("isbn.duplicate"),
    ERROR_FAILED_DELETED("error.failed.delete"),
    ERROR_GENERAL("error.general"),


    //   GENERAL ERROR
    FAILED_TO_PROCESS("failed.to.process"),

    //  AUTH
    SUCCESS_SIGNUP("success.signup"),
    SUCCESS_SIGNIN("success.signin"),
    SUCCESS_LOGOUT("success.logout");



    private final String key;

    MessageKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}