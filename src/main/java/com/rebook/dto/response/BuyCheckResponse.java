package com.rebook.dto.response;

import com.rebook.domain.BuyDecisionResult;

public class BuyCheckResponse {
    private boolean buyable;
    private String message;
    private String rejectReason;

    public BuyCheckResponse() {
    }

    public BuyCheckResponse(boolean buyable, String message, String rejectReason) {
        this.buyable = buyable;
        this.message = message;
        this.rejectReason = rejectReason;
    }

    public boolean isBuyable() {
        return buyable;
    }

    public String getMessage() {
        return message;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public static BuyCheckResponse from(BuyDecisionResult result) {
        return new BuyCheckResponse(result.isBuyable(), result.getMessage(), result.getRejectReason());
    }


}
