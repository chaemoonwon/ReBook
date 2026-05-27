package com.rebook.service;

import com.rebook.domain.AnswerType;
import com.rebook.domain.BookConditionResponse;
import com.rebook.domain.ResponseEvaluationResult;

public class BuyDecisionService {

    public ResponseEvaluationResult evaluateResponse(BookConditionResponse response) {

        if (response.getAnswerType() != AnswerType.YES) {
            return new ResponseEvaluationResult(false, "");
        }
        if (response.getQuestion().contains("찢어짐")) {
            return new ResponseEvaluationResult(true, "2cm 초과의 심한 찢어짐이 있어 매입할 수 없습니다.");
        } else if (response.getQuestion().contains("곰팡이")) {
            return new ResponseEvaluationResult(true, "곰팡이가 있어 매입할 수 없습니다.");
        } else if (response.getQuestion().contains("오염")
                || response.getQuestion().contains("낙서")) {
            return new ResponseEvaluationResult(true, "심한 오염 또는 심한 낙서가 있어 매입할 수 없습니다.");
        } else if (response.getQuestion().contains("물")
                || response.getQuestion().contains("젖은")) {
            return new ResponseEvaluationResult(true, "물에 젖은 흔적이 있어 매입할 수 없습니다.");
        } else if (response.getQuestion().contains("페이지 누락")
                || response.getQuestion().contains("제본 불량")
                || response.getQuestion().contains("분리")) {
            return new ResponseEvaluationResult(true, "페이지 누락 또는 제본 불량으로 책이 분리되어 매입할 수 없습니다.");
        }

        return new ResponseEvaluationResult(false, "");
    }
}
