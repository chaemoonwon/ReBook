package com.rebook.dto.request;

import com.rebook.domain.AnswerType;

public class BuyCheckAnswerRequest {
    private Long questionId;
    private AnswerType answerType;

    public BuyCheckAnswerRequest() {
    }

    public Long getQuestionId() {
        return questionId;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }
}
