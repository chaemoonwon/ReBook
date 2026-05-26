package com.rebook.domain;

public class ResponseEvaluationResult {

    private final boolean rejected;
    private final String rejectReason;

    public ResponseEvaluationResult(boolean rejected, String rejectReason) {
        this.rejected = rejected;
        this.rejectReason = rejectReason;
    }

    public boolean isRejected() {
        return rejected;
    }

    public String getRejectReason() {
        return rejectReason;
    }
}
