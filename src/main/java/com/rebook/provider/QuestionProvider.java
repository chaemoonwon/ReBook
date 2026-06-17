package com.rebook.provider;

import com.rebook.domain.BookConditionQuestion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class QuestionProvider {

    private final List<BookConditionQuestion> questions = List.of
            (
                    new BookConditionQuestion(
                            1L,
                            "2cm 초과의 심한 찢어짐이 있나요?",
                            "2cm 초과의 심한 찢어짐이 있어 매입할 수 없습니다."
                    ),
                    new BookConditionQuestion(
                            2L,
                            "곰팡이가 있나요?",
                            "곰팡이가 있어 매입할 수 없습니다."
                    ),
                    new BookConditionQuestion(
                            3L,
                            "심한 오염 또는 심한 낙서가 있나요?",
                            "심한 오염 또는 심한 낙서가 있어 매입할 수 없습니다."
                    ),
                    new BookConditionQuestion(
                            4L,
                            "물에 젖은 흔적이 있나요?",
                            "물에 젖은 흔적이 있어 매입할 수 없습니다."
                    ),
                    new BookConditionQuestion(
                            5L,
                            "페이지 누락 또는 제본 불량으로 책이 분리되어 있나요?",
                            "페이지 누락 또는 제본 불량으로 책이 분리되어 매입할 수 없습니다."
                    )
            );

    public List<BookConditionQuestion> getQuestions() {
        return questions;
    }

    public Optional<BookConditionQuestion> findById(Long questionId) {

        return getQuestions().stream()
                .filter(question -> Objects.equals(question.getQuestionId(), questionId))
                .findFirst(); //고정되어 있으므로 findFirst()를 반환한다.
    }
}
