package com.rebook.domain;

public class BookConditionQuestion {
    //질문 문장
    private final String content;
    //매입 불가 사유
    private final String rejectedReason;

    public BookConditionQuestion(String question, String rejectedReason) {
        this.content = question;
        this.rejectedReason = rejectedReason;
    }

    public String getContent() {
        return content;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }
}
