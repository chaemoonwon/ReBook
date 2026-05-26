package com.rebook.domain;

public class BookConditionResponse {
    private final String question;
    private final AnswerType answerType;

    public BookConditionResponse(String question, AnswerType answerType) {
        this.question = question;
        this.answerType = answerType;
    }

    public String getQuestion() {
        return question;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }
}
