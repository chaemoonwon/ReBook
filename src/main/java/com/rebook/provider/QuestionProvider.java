package com.rebook.provider;

import com.rebook.domain.BookConditionQuestion;

import java.util.List;

public class QuestionProvider {

    private final List<BookConditionQuestion> questions = List.of
            (
                    new BookConditionQuestion(
                            "2cm 초과의 심한 찢어짐이 있나요?",
                            "2cm 초과의 심한 찢어짐이 있어 매입할 수 없습니다."
                    ),
                    new BookConditionQuestion(
                            "곰팡이가 있나요?",
                            "곰팡이가 있어 매입할 수 없습니다."
                    ),
                    new BookConditionQuestion(
                            "심한 오염 또는 심한 낙서가 있나요?",
                            "심한 오염 또는 심한 낙서가 있어 매입할 수 없습니다."
                    ),
                    new BookConditionQuestion(
                            "물에 젖은 흔적이 있나요?",
                            "물에 젖은 흔적이 있어 매입할 수 없습니다."
                    ),
                    new BookConditionQuestion(
                            "페이지 누락 또는 제본 불량으로 책이 분리되어 있나요?",
                            "페이지 누락 또는 제본 불량으로 책이 분리되어 매입할 수 없습니다."
                    )
            );

    public List<BookConditionQuestion> getQuestions() {
        return questions;
    }
}
