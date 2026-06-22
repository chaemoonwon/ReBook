package com.rebook.service;

import com.rebook.domain.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuyDecisionServiceTest {

    BuyDecisionService service = new BuyDecisionService();

    @Test
    public void YES_응답이면_매입불가_결과를_반환한다() {
        BookConditionQuestion question = new BookConditionQuestion(1L, "곰팡이가 있나요?", "곰팡이가 있어 매입이 어렵습니다.");
        BookConditionResponse response = new BookConditionResponse(question, AnswerType.YES);
        ResponseEvaluationResult result = service.evaluateResponse(response);

        assertTrue(result.isRejected());
        assertEquals(response.getQuestion().getRejectedReason(), result.getRejectReason());

    }

    @Test
    public void NO_응답이면_매입가능_결과를_반환한다(){
        BookConditionQuestion question = new BookConditionQuestion(1L, "곰팡이가 있나요?", "곰팡이가 있어 매입이 어렵습니다.");
        BookConditionResponse response = new BookConditionResponse(question, AnswerType.NO);
        ResponseEvaluationResult result = service.evaluateResponse(response);

        assertFalse(result.isRejected());
        assertEquals("", result.getRejectReason());

    }

    @Test
    public void OTHER_응답이면_매입가능_결과를_반환한다(){
        BookConditionQuestion question = new BookConditionQuestion(1L, "곰팡이가 있나요?", "곰팡이가 있어 매입이 어렵습니다.");
        BookConditionResponse response = new BookConditionResponse(question, AnswerType.OTHER);
        ResponseEvaluationResult result = service.evaluateResponse(response);

        assertFalse(result.isRejected());
        assertEquals("", result.getRejectReason());

    }
}