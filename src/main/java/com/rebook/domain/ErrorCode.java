package com.rebook.domain;

public enum ErrorCode {
    INVALID_QUESTION_ID("INVALID_QUESTION_ID", "존재하지 않는 ID입니다."),
    INVALID_REQUEST("INVALID_REQUEST", "잘못된 요청 입니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
