package com.rebook.view;

import com.rebook.domain.BuyDecisionResult;


public class OutputView {

    public void printQuestion(String question) {
        System.out.println(question);
        System.out.println("1. 예");
        System.out.println("2. 아니오");
        System.out.println("3. 기타");
    }


    public void printResult(BuyDecisionResult result) {
        System.out.println(result.getMessage());
        if (!result.isBuyable()) {
            System.out.println("사유: " + result.getRejectReason());
        }
    }
}
