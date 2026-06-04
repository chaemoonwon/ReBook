package com.rebook.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class AnswerTypeTest {


    @Test
    void 입력값_1이면_YES를_반환한다() {
        String input = "1";
        AnswerType answerType = AnswerType.fromInput(input);
        Assertions.assertEquals(AnswerType.YES, answerType);
    }

    @Test
    void 입력값_2이면_NO를_반환한다() {
        String input = "2";
        AnswerType answerType = AnswerType.fromInput(input);
        Assertions.assertEquals(AnswerType.NO, answerType);
    }

    @Test
    void 입력값_3이면_OTHER를_반환한다() {
        String input = "3";

        AnswerType answerType = AnswerType.fromInput(input);
        Assertions.assertEquals(AnswerType.OTHER, answerType);
    }

    @Test
    void 잘못된_입력이면_null을_반환한다() {
        String input = "4";
        AnswerType answerType = AnswerType.fromInput(input);
        Assertions.assertNull(answerType);
    }
}