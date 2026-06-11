package com.rebook.provider;

import com.rebook.domain.BookConditionQuestion;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


class QuestionProviderTest {

    @Test
    void 존재하는_id이면_질문을_반환한다() {
        //given
        QuestionProvider provider = new QuestionProvider();

        //when
        Optional<BookConditionQuestion> result = provider.findById(1L);

        //then
        assertThat(result).isPresent();

        BookConditionQuestion question = result.get();
        assertThat(question.getQuestionId()).isEqualTo(1L);
        assertThat(question.getContent()).isEqualTo("2cm 초과의 심한 찢어짐이 있나요?");
        assertThat(question.getRejectedReason()).isEqualTo("2cm 초과의 심한 찢어짐이 있어 매입할 수 없습니다.");

    }

    @Test
    void 존재하지_않는_id이면_빈_Optional을_반환한다() {
        //given
        QuestionProvider provider = new QuestionProvider();

        //when
        Optional<BookConditionQuestion> result = provider.findById(999L);

        //then
        assertThat(result).isEmpty();

    }


}