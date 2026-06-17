package com.rebook.service;

import com.rebook.domain.AnswerType;
import com.rebook.domain.BookConditionResponse;
import com.rebook.domain.ResponseEvaluationResult;
import org.springframework.stereotype.Service;

@Service
public class BuyDecisionService {

    public ResponseEvaluationResult evaluateResponse(BookConditionResponse response) {

        if (response.getAnswerType() != AnswerType.YES) {
            return new ResponseEvaluationResult(false, "");
        }

        return new ResponseEvaluationResult(
                true,
                response.getQuestion().getRejectedReason());
    }
}
