package com.rebook.domain;

public class BookConditionQuestion {
    //질문 id
    private final Long questionId;
    //질문 문장
    private final String content;
    //매입 불가 사유
    private final String rejectedReason;

    public BookConditionQuestion(Long questionId,String content, String rejectedReason) {
        this.questionId = questionId;
        this.content = content;
        this.rejectedReason = rejectedReason;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getContent() {
        return content;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }
}
