package com.rebook.dto.response;

import com.rebook.domain.ErrorCode;

public class ErrorResponse {
    private final String code;
//    private final String code;
    private final String message;

    public ErrorResponse(ErrorCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
