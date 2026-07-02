package com.rebook.dto.request;

import com.rebook.domain.AnswerType;
import jakarta.validation.constraints.NotNull;

public class BuyCheckAnswerRequest {

    @NotNull
    private Long questionId;

    @NotNull
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
