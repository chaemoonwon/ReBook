package com.rebook.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class BuyCheckRequest {

    @NotBlank
    private String bookTitle;

    @Valid
    @NotEmpty
    private List<BuyCheckAnswerRequest> answers;

    public BuyCheckRequest() {
    }

    public BuyCheckRequest(String bookTitle, List<BuyCheckAnswerRequest> answers) {
        this.bookTitle = bookTitle;
        this.answers = answers;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public List<BuyCheckAnswerRequest> getAnswers() {
        return answers;
    }



}
