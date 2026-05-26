package com.rebook.service;

import com.rebook.domain.BookConditionResponse;
import com.rebook.domain.ResponseEvaluationResult;

public class BuyDecisionService {

    public ResponseEvaluationResult evaluateResponse(BookConditionResponse bookConditionResponse) {
        return new ResponseEvaluationResult(false, "");
    }
}