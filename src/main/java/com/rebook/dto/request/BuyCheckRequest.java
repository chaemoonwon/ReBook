package com.rebook.dto.request;

import java.util.List;

public class BuyCheckRequest {
    private String bookTitle;
    private List<BuyCheckAnswerRequest> answers;

    public BuyCheckRequest() {
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public List<BuyCheckAnswerRequest> getAnswers() {
        return answers;
    }
}
