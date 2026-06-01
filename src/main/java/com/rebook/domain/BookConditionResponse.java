package com.rebook.domain;

public class BookConditionResponse {
    private final BookConditionQuestion question;
    private final AnswerType answerType;

    public BookConditionResponse(BookConditionQuestion question, AnswerType answerType) {
        this.question = question;
        this.answerType = answerType;
    }

    public BookConditionQuestion getQuestion() {
        return question;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }
}
