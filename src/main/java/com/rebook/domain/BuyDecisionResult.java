package com.rebook.domain;

public class BuyDecisionResult {

    private final boolean buyable;
    private final String rejectReason;
    private final String message;

    public BuyDecisionResult(boolean buyable, String rejectReason, String message) {
        this.buyable = buyable;
        this.rejectReason = rejectReason;
        this.message = message;
    }

    public boolean isBuyable() {
        return buyable;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public String getMessage() {
        return message;
    }
}
