package com.rebook.dto.request;

import com.rebook.domain.AnswerType;

public class BuyCheckAnswerRequest {
    private Long questionId;
    private AnswerType answerType;

    public BuyCheckAnswerRequest() {
    }

    public BuyCheckAnswerRequest(Long questionId, AnswerType answerType) {
        this.questionId = questionId;
        this.answerType = answerType;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }
}
