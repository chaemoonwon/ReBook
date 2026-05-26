package com.rebook.provider;

import java.util.List;

public class QuestionProvider {

    private final List<String> questions = List.of(
            "2cm 초과의 심한 찢어짐이 있나요?",
            "곰팡이가 있나요?",
            "심한 오염 또는 심한 낙서가 있나요?",
            "물에 젖은 흔적이 있나요?",
            "페이지 누락 또는 제본 불량으로 책이 분리되어 있나요?"
    );

    public List<String> getQuestions() {
        return questions;
    }
}
